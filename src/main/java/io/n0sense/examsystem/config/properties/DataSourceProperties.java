package io.n0sense.examsystem.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.datasource")
public class DataSourceProperties {
    /**
     * MySQL服务器的主机IP地址
     */
    private String host = "127.0.0.1";
    /**
     * 当前使用数据库的表名。
     */
    private String tableName;
    /**
     * MySQL的安装所在路径。
     */
    private String mysqlHome;
    /**
     * 数据库备份文件的保存位置。
     */
    private String backupLocation = "./db_backup";
}
