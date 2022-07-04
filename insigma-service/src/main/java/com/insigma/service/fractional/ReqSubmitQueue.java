package com.insigma.service.fractional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ReqSubmitQueue
 * @Description
 * @Author carrots
 * @Date 2022/7/4 13:56
 * @Version 1.0
 */
@Component
public class ReqSubmitQueue {
    @Autowired
    OrderProcessor orderProcessor;

    private ThreadPoolExecutor threadPoolExecutor;

    @PostConstruct
    private void init() {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueSize), r -> new Thread(r, "fractional-order-submid-thread"), (r, executor) -> {
        });
    }

    public void enqueue(OmsReq req) {
        FractionalReqSubmitTask task = new FractionalReqSubmitTask(req, this.apaxFractionalApiCaller, this.orderProcessor);
        threadPoolExecutor.submit(task);
    }

    class FractionalApiSubmitTask extends Thread{
        private OmsReq req;
        private ApexFractionalApiCaller apexFractionalApiCaller;

        private OrderProcessor orderProcessor;
        public FractionalApiSubmitTask(Omsreq omsreq, ApexFractionalApiCall apexFractionalApiCall, OrderProcessor orderProcessor) {
            this.req = omsreq;
            this.apexFractionalApiCaller = apexFractionalApiCall;
            this.orderProcessor = orderProcessor;
        }

        @Override
        public void run() {
            try {
                if (req instanceof OmsFractionalPlaceOrderReq) {
                    ReqSyncResp syncResp = this.apexFractionalApiCaller.placeOrder(req);
                    this.orderProcessor.omsOrderBackReport(syncResp, req);
                } else {

                }
            } catch (Exception e) {

            }
        }
    }
}
