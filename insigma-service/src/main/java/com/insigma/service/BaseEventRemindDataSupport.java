package main.java.com.insigma.service;

import com.alibaba.fastjson.JSON;
import jdk.internal.net.http.common.Log;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BaseEventRemindDataSupport
 * @Description
 * @Author carrots
 * @Date 2022/6/22 16:09
 * @Version 1.0
 */
@Component
@Slf4j
public abstract class BaseEventRemindDataSupport<T> {
    private static final int EVENT_REMIND_YEAR_LIMIT = 5;

    @Resource
    private EventRemindMapper eventRemindMapper;

    public List<T> convertSourceDataList(String businessName, T sourceData, Class<T> clazz) {
        List<T> result = JSON.parseArray(sourceData.toString(), clazz);
        log.info(businessName, "转换后的数据" + result.toString());
        List<SecurityInfoDto> list = getSecurityList(sourceData);
        return result;
    }

    public abstract List<SecurityInfoDto> getSecurityList(T sourceData);

}
