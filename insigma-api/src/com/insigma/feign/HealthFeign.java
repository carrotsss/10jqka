package com.insigma.feign;

import com.insigma.bean.BasicHealthInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName HealthFeign
 * @Description TODO
 * @Author carrots
 * @Date 2022/6/16 11:14
 * @Version 1.0
 */
@FeignClient(name = "insigma.health")
public interface HealthFeign {
    @GetMapping("/listByVersion")
    List<BasicHealthInfo> getListByVersion(@RequestParam("version") @Valid String version);
}
