package cn.itcast.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/17  11:21
 */
@Data
@Component
@ConfigurationProperties("pattern")
public class PatternProperties {
    private String dateformat;
    private String envSharedValue;
}
