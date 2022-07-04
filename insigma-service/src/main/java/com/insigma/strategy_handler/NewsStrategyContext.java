package com.insigma.strategy_handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NewsStrategyContext
 * @Description
 * @Author carrots
 * @Date 2022/7/4 10:14
 * @Version 1.0
 */
@Component
public class NewsStrategyContext {
    public Map<String, NewsStrategy> context = new HashMap<>();
    @Autowired
    public NewsFlashStrategy newsFlashStrategy;
    @Autowired
    private ArticleStategy articleStategy;

    @PostConstruct
    public void init() {
        this.context.put(OutDataNewsType.NEWS_FLASH.getType(), newsFlashStrategy);
        this.context.put(OutDataNewsType.ARTICLE.getType(), articleStategy);
    }
}
