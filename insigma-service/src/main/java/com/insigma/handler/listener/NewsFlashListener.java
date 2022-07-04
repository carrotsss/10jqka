package com.insigma.handler.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName NewsFlashListener
 * @Description
 * @Author carrots
 * @Date 2022/7/1 16:41
 * @Version 1.0
 */
@Component
public class NewsFlashListener {
    public static final String LOG_MESSAGE1 = "newsToSource" +;
    public static final String LOG_MESSAGE2 = "newsTpRObot";
    @Value("${wencai.news-flash.topic}")
    private String wenCaiTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    @Qualifier("wenCaiKafkaTemplate")
    private KafkaTemplate<String, String> wenCaiKafkaTemplate;

    @EventListener(value = ApplicationEvent.class)
    @Async
    public void newSourceListener(ApplicationEvent event) {
        NewsFlashDto news = (NewsFlashDto) event.getSource();
        try {
            log.info();
            List<Long> tagIds = news.getTags().stream().map(NewsFlashLabel::getId).collect(Collectors.toList());
            if (tagIds.contains(NewFlashConstant.ARK_TAG_ID)) {
                return;
            }
        } catch (Exception e) {
            log.error();
        }

    }

    /**
     * publisher.publish(new NewsFlashEvent(news))
     * @param event
     */
    @EventListener(value = ApplicationEvent.class)
    @Async
    public void newsWenCaiListener(ApplicationEvent event) {
        NewsFlashDto newsFlashDto = (NewsFlashDto) event.getSource();
        NewsFlashVo vo = newFlashUtils.objectChange(newsFlashDto);
        wenCaiKafkaTemplate.send(wenCaiTopic, OvseJson.toJson(vo));
    }
}
