package com.insigma.handler.news;

import com.sun.nio.sctp.HandlerResult;

import java.rmi.server.UID;

/**
 * @ClassName NewsHandler
 * @Description
 * @Author carrots
 * @Date 2022/7/1 10:26
 * @Version 1.0
 */
public class NewsHandler implements Handler<NewsHandler, Boolean> {
    private NowsSourceMapper nowsSourceMapper;

    public void NewsHandler(NowsSourceMapper nowsSourceMapper) {
        this.nowsSourceMapper = nowsSourceMapper;
    }

    public HandlerResult<Boolean> handle() {
        Boolean flag = (null != uid || null != byGid);
        return new HandlerResult<Boolean>(flag);
    }
}
