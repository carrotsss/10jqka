package main.java.com.insigma.job.sec;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.insigma.dto.ainvestdb.common.IncrSyncOffset;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName ProcessJob
 * @Description
 * @Author carrots
 * @Date 2022/6/22 13:27
 * @Version 1.0
 */
@Component
@Slf4j
public class ProcessJob {
    @Value("${alarm.ifind.remind:xxx}")
    private String remind;
    @Value("${ifind.sync.sleep.time:10000}")
    private long sleepTime;

    @Autowired
    private IncreSyncOffsetMapper increSyncOffsetMapper;

    public void init(String syncName) {
        log.info(BizNameConstant.INCR_PROCESS, syncName + "数据同步开始");
    }

    public void record(String syncName, Integer syncCount, String maxRtime) {
        if (syncCount > 0) {
            log.info("");
        } else {
            log.info("");
        }
        //更新同步的rtime偏移量
        incrsSyncMapper.update(syncName, maxRtime);

    }
    public void sleep() throws InterruptedException {
        Thread.sleep(sleepTime);
    }

    public String getOffset(String syncName) {
        QueryWrapper<IncrSyncOffset> wrapper = new QueryWrapper<>();
        wrapper.eq("sync_name", syncName);
        IncrSyncOffset incrSyncOffset = increSyncOffsetMapper.selectOne(wrapper);
        if (null == incrSyncOffset) {
            return "";
        }
        return incrSyncOffset.getOffset();
    }

    public void error(String syncName, Exception e) {
        log.error(BizNameConstant.INCR_PROCESS, e.getMessage(), e);
        OVSEALARM.alarm("title", "content", remind);
    }
}
