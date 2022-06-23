package main.java.com.insigma.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @ClassName ExtColumns
 * @Description
 * @Author carrots
 * @Date 2022/6/22 15:27
 * @Version 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ext.sync.sod")
public class ExtColumns {
    private Map<String, String> column;
}
