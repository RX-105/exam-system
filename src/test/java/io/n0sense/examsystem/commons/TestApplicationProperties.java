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
