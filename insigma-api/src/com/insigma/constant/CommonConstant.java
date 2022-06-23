package com.insigma.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @ClassName CommonConstant
 * @Description TODO
 * @Author carrots
 * @Date 2022/6/16 9:24
 * @Version 1.0
 **/
@Configuration
public class CommonConstant {
    public static int HTTP_RETRY_REQUEST;
    public static boolean HTTP_RETRY_ACTION;
    public static String Common_Exception;

    @Value("${spring.application.name}")
    public void setApplicationName(String value) {
        HTTP_RETRY_REQUEST = Integer.parseInt(value);
    }
}
