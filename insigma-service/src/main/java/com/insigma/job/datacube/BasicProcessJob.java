package com.insigma.job.datacube;

import main.java.com.insigma.util.BasicProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName BasicProcessJob
 * @Description
 * @Author carrots
 * @Date 2022/6/22 15:07
 * @Version 1.0
 */
@Component
@Slf4j
public class BasicProcessJob {
    private static final String CONTENT_FIELD_BIZ_TRACE_ID = "bizTraceId";
    private static final String CONTENT_FIELD_DEAL_DESCRIPTION = "dealDescription";
    @Autowired
    private BasicShareHoldService basicShareHoldService;

    @OvseJobHandler("share-hold-summary")
    public void HandleShareHoldSummary(OvseJobParam param) {
        String bizNmae = "BasicProcessJob.handleShareHoldSummary";
        log.info(bizNmae, "f10-证券持仓汇总数据同步开始");
        String bizTraceId = BasicProcessUtil.genrateBiztraceId();
        LogItem logItem = LogItem.getInstance();
        logItem.add(CONTENT_FIELD_BIZ_TRACE_ID, bizTraceId);
        Map<String, Object> resultMap = basicShareHoldService.loadStafHoldProcess(bizTraceId);
        log.info(bizNmae, "f10-内部人交易月度数据同步结束" + resultMap.get(CONTENT_FIELD_BIZ_TRACE_ID), logItem);
    }
}
