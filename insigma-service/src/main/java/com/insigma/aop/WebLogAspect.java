package com.insigma.aop;

import io.netty.handler.codec.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @ClassName WebLogAspect
 * @Description
 * @Author carrots
 * @Date 2022/6/30 15:26
 * @Version 1.0
 */
@Component
@Aspect
@Slf4j
public class WebLogAspect {
    public static final int PRINT_LENGTH = 200;

    @Pointcut("execution(public * com.myhexin.news.controller.*Controller.*(..))")
    private void pointcut() {

    }

    @Pointcut("execution(public * com.myhexin.basic.controller.*.*Controller.*(..))")
    private void basicPointcut() {

    }

    @Around("point() || basicPointcut()")
    public Object handleAround(ProceedingJoinPoint joinPoint) {
        String webLogTraceId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        JSONObject preHandleObj = new JSONObject();
        String reqParam = null;
        String uri = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes != null) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                reqParam = getRequestParamStr(request);
                uri = request.getRequestURI();
                preHandleObj.put("requesterIp", HttpUtil.getRemoteAddr(request));
                preHandleObj.put("requestParam ", reqParam);
                request.setAttribute("webLogTraceId ", webLogTraceId);
            }
        }
        //打印请求开始日志
        ElkUtils.info(webLogTraceId, ElkBusinessNameEnum.WEB_BASIC_LOG.getType(), null, preHandleObj);
        Object result = joinPoint.proceed();
        //打印请求结束日志
        ElkUtils.info();
        return result;
    }

}
