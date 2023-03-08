package io.n0sense.examsystem.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    /**
     * 应用版本。
     */
    private String version;
    /**
     * 特殊管理权限使用的密码。
     */
    private String adminToken;
    /**
     * 程序产生的数据保存位置。
     */
    private String appDataLocation;
}
