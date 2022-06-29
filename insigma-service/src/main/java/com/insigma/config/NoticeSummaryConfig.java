package com.insigma.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.function.Function;

/**
 * @ClassName NoticeSummaryConfig
 * @Description
 * @Author carrots
 * @Date 2022/6/29 16:33
 * @Version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "notice.summary")
public class NoticeSummaryConfig {
    private Map<String, NoticeSummary> mapping;
    private List<NoticeSummary> list;

    public Map<String, NoticeSummary> getSummaryMapping() {
        if (CollectionUtils.isEmpty(mapping) || CollectionUtils.isEmpty(list)) {
            return mapping;
        }
        mapping = list.stream().toMap(NoticeSummary::getId, Function.identity(), (v1, v2) -> v2);
        return mapping;
    }
}
