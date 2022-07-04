package com.insigma.strategy_handler;

import com.insigma.config.thread.TaskAysncExecutorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.text.AbstractDocument;

/**
 * @ClassName ArticleStategy
 * @Description
 * @Author carrots
 * @Date 2022/7/4 10:01
 * @Version 1.0
 */
@Component
public class ArticleStategy implements NewsStrategy {
    public S3FileStrore s3FileStrore;
    public String prefix;
    public String url;
    @Autowired
    private TaskAysncExecutorConfig taskAyncExcutorConfig;

    public ArticleStategy(S3FileStoreHolder s3FileStoreHolder, @Value("${aritcle.picture.prefix}") String prefix, @Value("article.push.url") String url) {
        this.s3FileStrore = s3FileStoreHolder.getStoreByname(NewsFlashConstant.S3_STORE_NAME);
        this.prefix = prefix;
        this.url = url;
    }

    @Override
    public void processNews(String constant) {
        taskAyncExcutorConfig.getAsyncExecutor().execute(() -> pushArticle(constant));
    }

    private void pushAritcle(String articleNewsData) {

    }
}
