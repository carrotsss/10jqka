package com.insigma.service.fractional;

import io.lettuce.core.api.push.PushListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderProcessorImpl
 * @Description
 * @Author carrots
 * @Date 2022/7/4 14:26
 * @Version 1.0
 */
@Service
public class OrderProcessorImpl {
    @Autowired
    ReqSubmitQueue reqSubmitQueue;
    public void placeOrder(OmsFractionalPlaceOrderReq req) throws Exception {
        if (!req.isLegal()) {
            throw new Exception();
        }
        this.reqSubmitQueue.enqueue(req);
    }


}
