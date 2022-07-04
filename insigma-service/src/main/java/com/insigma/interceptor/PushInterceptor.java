package com.insigma.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.SerializableString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName PushInterceptor
 * @Description
 * @Author carrots
 * @Date 2022/7/4 11:06
 * @Version 1.0
 */
public class PushInterceptor implements HandlerInterceptor {
    @Value("${verification.appId}")
    private String appId;

    @Value("${verification.appSecret}")
    private String appSecret;
    private long timeout = 60L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        JSONObject jsonObject = new JSONObject();
        String signValue = request.getParameter("en_horse");
        String appIdValue = request.getParameter("appId");
        String timestamp = request.getParameter("timestamp");

        return true;
    }
}
