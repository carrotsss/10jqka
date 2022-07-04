package com.insigma.job.news.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName JobHandler
 * @Description
 * @Author carrots
 * @Date 2022/7/1 10:38
 * @Version 1.0
 */
@Component
public class JobHandler {
    @Autowired
    private BlockNewsInitJob blockNewsInitJob;

    @OvseJobHandler("block-news-init")
    public void blockNewsInit(OvseJobParam param) {
        String jobCode = BlockNewsInitJob.JOB_CODE;
        dealWithJobFunction(jobCode, JobUniqueFlag.OVSEALL, () -> blockNewsInitJob.handle());
    }

    private void dealWithJobFunction(String jobCode, JobUniqueFlag jobUniqueFlag, JobHandleFunction jobHandleFunction) {
        jobHandleFunction.handle();
    }

}
