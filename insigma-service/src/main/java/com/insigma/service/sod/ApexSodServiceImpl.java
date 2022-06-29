package com.insigma.service.sod;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.insigma.constant.SodConstant;
import com.insigma.handler.sod.ApexSodSftpOperator;
import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.jute.Record;
import org.assertj.core.util.Lists;
import org.checkerframework.checker.units.qual.A;
import org.jboss.netty.channel.ExceptionEvent;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.File;
import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @ClassName ApexSodServiceImpl
 * @Description
 * @Author carrots
 * @Date 2022/6/29 10:41
 * @Version 1.0
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "cleanConfigCacheManager")
public class ApexSodServiceImpl implements ClearingManager {
    @Autowired
    private ApexSodSftpOperator sodSftpOperator;

    @Autowired
    private SodDownloadRecordService sodDownloadRecordService;

    @Autowired
    private EbusClient ebusClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Value("${Apex.sod.download.delay:30000}")
    private long downloadDelay;

    @Value("${apex.sod.local.path:/sod}")
    private String sodLocalPath;

    @Autowired
    private S3FileStoreHolder s3FileStoreHolder;

    private volatile FileStore fileStore;

    private static final String S3_STORE_NAME = "pri-omstrade";

    public static final String FINISH_NOTIFY_LOCK = "sod-download-finish-notify-lock";
    public static final String FINISH_NOTIFY_CACHE = "sod-download-finish-notify-cache";

    public static long LOCK_TIMEOUT = 10;

    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(150),
            r -> new Thread(r, "sod-download-thrad"),
            (r, executor) -> {
                ovseAlam.alarm();
            });

    @Override
    public List<SftpFile> getNewComeFiles(String version) {
        List<SftpFile> sftpFiles = sodSftpOperator.scanAll(ApexSodSftpOperator.getScanPath(version));
        if (CollectionUtils.isEmpty(sftpFiles)) {
            return sftpFiles;
        }

        Map<String, SodDownloadRecord> existed = sodDownloadRecordService.listByCreateDate()
                .stream().collect(Collectors.toMap(sodDownloadRecordService::getSftpPath, e -> e));

        List<SftpFile> newCome = new ArrayList<>();
        List<SftpFile> changed = new ArrayList<>();
        List<SftpFile> refresh = new ArrayList<>();

        for (SftpFile sftpFile : sftpFiles) {
            SodDownloadRecord record = existed.get(sftpFile.getAbsoluteFileName());
            if (record == null) {
                long interval = System.currentTimeMillis() - sftpFile.getModifyTime() * 1000L;
                if (interval > downloadDelay) {
                    newCome.add(sftpFile);
                } else {
                    log.error("");
                }
            }
        }
        return newCome;
    }

    private void finish(String message) {
        RLock lock = redissonClient.getLock(FINISH_NOTIFY_LOCK);
        try {
            lock.lock(LOCK_TIMEOUT, TimeUnit.SECONDS);
            Object cacheObject = stringRedisTemplate.opsForValue().get(FINISH_NOTIFY_CACHE);
            String cache = "";
            if (cacheObject != null) {
                cache = cacheObject.toString();
            }
            cache = "\n\t" + message;
            stringRedisTemplate.opsForValue().set(FINISH_NOTIFY_CACHE, cache);
        } catch (Exception e) {
            log.warn("");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void sodDownloadTask(List<SodDownloadRecord> records) {
        for (SodDownloadRecord record : records) {
            SodDownloadTask sodDownloadTask = new SodDownloadTask(record, sodDownloadRecordService);
            executor.submit(sodDownloadTask);
        }
    }

    @Override
    public FileStore getSodFileStore() {
        if (this.fileStore != null) {
            return this.fileStore;
        }
        synchronized (ApexSodServiceImpl.class) {
            if (this.fileStore != null) {
                return this.fileStore;
            }
            this.fileStore = s3FileStoreHolder.getStartByName(S3_STORE_NAME);
        }
        return this.fileStore;
    }

    class sodDownloadTask extends Thread {
        private SodDownloadRecord sodDownloadRecord;
        private SodDownloadReordService sodDownloadReordService;
        private FileStore s3FileStore;
        private ApexSodSftpOperator apexSodSftpOperator;

        public sodDownloadTask(SodDownloadRecord sodDownloadRecod,
                               SodDownloadReordService sodDownloadReordService,
                               FileStore s3FileStore,
                               ApexSodSftpOperator apexSodSftpOperator) {
            this.sodDownloadRecord = sodDownloadRecod;
            this.sodDownloadReordService = sodDownloadReordService;
            s3FileStore = s3FileStore;
            apexSodSftpOperator = apexSodSftpOperator;
        }

        @Override
        public void run() {
            log.info("");
            //乐观锁尝试
            int update = sodDownloadReordService.updateStateInList(SodDownloadState.LOADING, Lists.newArrayList(SodDownloadState.INIT, SodDownloadState.REFRESH), sodDownloadRecord);
            if (update == 0) {
                log.warn();
                return;
            }
            String s3Path = null;
            try {
                String filePath = sodLocalPath + sodDownloadRecod.getSftpPath();
                sodSftpOperator.download(sodDownloadRecord.getSftpPath(), filePath);
                File localFile = new File(filePath);
                s3Path = SodConstant.S3_SOD_BASE_PATH + sodDownloadRecord.getSftpPath();
                s3FileStore.saveFile(s3Path, localFile);
                sodDownloadReordService.updateWhenFinish(s3Path, sodDownloadRecord.getId());
                localFile.delete();
            } catch (Exception e) {
                log.error("");
                OvseAlarm.alarm();
                sodDownloadRecordService.updateStateInList();
                return;
            }

            try {
                SodDownloaEvent event = new SodDownloadEvent();
                ebusClient.product(SodConstant.SOD_DOWNLOAD_EBUS_TOPIC, JSON.parseArray());

            } catch (Exception e) {
                log.error("");
            }

            if (Record.getFileType().startwith("EXT")) {
                finish(sodDownloadRecord.getFileType() + ":" + sodDownloadRecord.getFileSize() + ":" + "Byte(s)");
            }
            log.info("end download sod");
        }
    }

    @Cacheable(key = "'AdjustBatchNo_'+#clearDate+'_'+#step+'_'+#adjustType", unless = "#result == ''")
    private String getBatchNo(Integer clearDate, ClearingStep step, AdjustType adjustType) {
        ClearingProcessStatus openProcess = clearingProcessStatusService.findOpenProcess();
        ClearingAdjustType clearingAdjustType = clearingAdjustTypeDao.selectOne(new LambdaQueryWrapper<>()
                .eq(ClearingAdjustType::getProcess, openProcess.getProcess().toString())
                .eq(ClearingAdjustType::getStep, step.toString())
        );
        if (clearingAdjustType != null) {
            return clearingAdjustType.getId();
        }
        return "";

    }
}
