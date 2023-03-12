package io.n0sense.examsystem.service;

import io.n0sense.examsystem.service.impl.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Optional;

@SpringBootTest
public class TestAdminService {
    @Autowired
    AdminService adminService;

    @Test
    void testExport() {
        Optional<Resource> list;
        try {
            list = adminService.exportExamUserInfo(100001L);
        } catch (IOException e) {
            System.out.println("无法获取文件。");
            e.printStackTrace();
        }
    }
}
