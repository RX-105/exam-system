package io.n0sense.examsystem.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.dev")
public class DevProperties {
    /**
     * 是否验证操作符合阶段设置，置为true则为需要检查，false表示不检查。默认值为true。
     */
    private Boolean validateStageTime = true;
}
