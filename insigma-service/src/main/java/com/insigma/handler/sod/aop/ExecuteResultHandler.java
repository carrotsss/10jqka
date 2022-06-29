package com.insigma.handler.sod.aop;

/**
 * @ClassName ExecuteResultHandler
 * @Description
 * @Author carrots
 * @Date 2022/6/28 17:12
 * @Version 1.0
 */
public class ExecuteResultHandler {
    public static void execAndLog(ClearingResultHandler clearingResultHandler, ClearingResultMesage clearingResultMesage) {
        long start = System.currentTimeMillis();
        ClearingStep step = clearingResultMesage.getType();
        LogItem logItem = ClearingUtil.createLogItemByMessage(clearingResultMesage);
        OvseStatsLog.info("result", logItem);
        //处理消费方法
        clearingResultHandler.handler(clearingResultMesage);
    }
}
