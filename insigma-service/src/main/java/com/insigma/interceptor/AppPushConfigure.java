package com.insigma.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName AppPushConfigure
 * @Description
 * @Author carrots
 * @Date 2022/7/4 11:04
 * @Version 1.0
 */
public class AppPushConfigure implements WebMvcConfigurer {
    @Bean
    public PushInterceptor getPushInterceptor() {
        return new PushInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getPushInterceptor()).addPathPatterns("/push/appPush");
    }
}
