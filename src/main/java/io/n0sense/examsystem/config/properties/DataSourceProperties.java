/*
 * This file is part of exam-system.
 *
 * exam-system is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * exam-system is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with exam-system. If not, see <https://www.gnu.org/licenses/>.
 */

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
