package io.n0sense.examsystem.entity;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

@Log
public class TestAdminBuilder {
    @Test
    void testBuilder(){
        Admin admin = Admin.build()
                .name("demo")
                .password("1234")
                .groupName("sudoers")
                .get();
        log.info("admin.name = " + admin.getName());
    }
}
