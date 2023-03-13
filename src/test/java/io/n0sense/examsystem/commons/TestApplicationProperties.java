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

package io.n0sense.examsystem.commons;

import io.n0sense.examsystem.config.properties.ApplicationProperties;
import io.n0sense.examsystem.config.properties.DataSourceProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestApplicationProperties {
    @Autowired
    ApplicationProperties properties;
    @Autowired
    DataSourceProperties dataSourceProperties;

    @Test
    public void getProperties() {
        System.out.println(properties.getAppDataLocation());
        System.out.println(dataSourceProperties.getMysqlHome());
    }
}
