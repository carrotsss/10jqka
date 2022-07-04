package main.java.com.insigma.job.sec;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName pub201ProcessJob
 * @Description
 * @Author carrots
 * @Date 2022/6/22 13:42
 * @Version 1.0
 */
public class pub201ProcessJob extends ProcessJob {
    @Autowired
    private Pub201Service pub201Service;

    @Autowired
    private InstitutionService institutionService;

    @OvseJobHandler("pub201-process-job")
    public void handle(OvseJobParam param) {
        init(ProcessNmaeConstant.PUB201_PORCESS);
        int syncCount = StaticVar.COUNT_INIT;
        List<Pub201> pub201List = null;
        String maxRtime = getOffset(ProcessNmaeConstant.PUB201_PROCESS);
        try {
            while (true) {
                //
                pub201List = pub201Service.getRtimePaged(maxRtime);
                if (CollectionUtils.isEmpty(pub201List)) {
                    break;
                }
                institutionService.pub201Sync(pub201List);
                syncCount += pub201List.size();
                maxRtime = pub201List.get(pub201List.size() - 1).getRtime();
                sleep();
                //当前rtime无记录就退出循环
                String seq = StartVar.START_SEQ;
                while (true) {
                    pub201List = pub201Service.getRtiemSeqPaged(maxRtime, seq);
                    if (CollectionUtils.isEmpty(pub201List)) {
                        break;
                    }
                    institutionService.pub201Sync(pub201List);
                    syncCount += pub201List.size();
                    seq = pub201List.get(pub201List.size() - 1).getSeq();
                    sleep();
                }
            }
        } catch (Exception e) {
            error(ProcessNameConstant.PUB201_PROCESS, e);
            return;
        }
        record(ProcessNameConstant.PUB201_PROCESS, syncCount, maxRtime);
    }

    @DSTransactional
    public void stockProcess(List<Pub205> pub205List) {
        secIssuanceInfoService.pub205Sync(pub205List);
    }


}
