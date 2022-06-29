package com.insigma;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName main.java.com.insigma.InsigmaServiceApplication
 * @Description
 * @Author carrots
 * @Date 2022/6/23 9:10
 * @Version 1.0
 */
@EnableOvseJob
@ComponentScan(basePackages = {
        "com.insigma",
        "com.insigma.oms"
})
@EnableFeignClients({
        "com.insigma.feign",
        "com.insigma.oms.feign"
})
@SpringBootApplication
@EnableSonwFlake
@EnableS3FileStore
@EnbaleOvseLocalCache
@EnableTransactionManagement
@MapperScan("com.jqka.ovse.litequotation.repository.mapper")
@EnableMethodCache(basePackages = "con.jqka.ovse.litequotation")
@EnableCreateCacheAnnotation
@EnableBizMark
@EnableCaching
@EnableEbus
@EnableOvseJob
p ublic class InsigmaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsigmaServiceApplication.class, args);
    }
}
