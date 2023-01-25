package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Admin;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestAdminRepository {
    @Autowired
    AdminRepository adminRepository;
    @Test
    @Order(1)
    void test1Insert(){
        Admin admin = new Admin(1002L, "demo2", "1234", "sudoers");
        adminRepository.save(admin);
    }

    @Test
    @Order(2)
    void test2Select(){
        Optional<Admin> admin = adminRepository.findById(1001L);
        admin.ifPresent(value -> System.out.println("name: " + value.getName() + ", group: " + value.getGroupName()));
        admin = adminRepository.findAdminByName("admin");
        admin.ifPresent(value -> System.out.println("name: " + value.getName() + ", group: " + value.getGroupName()));
    }

    @Test
    @Order(3)
    void test3Update(){
        Optional<Admin> admin = adminRepository.findById(1001L);
        admin.ifPresent(value ->{
            value.setName("modified");
            adminRepository.save(value);
        });
    }

    @Test
    @Order(4)
    void test4Delete(){
        adminRepository.deleteById(1001L);
    }

    @Test
    void testExists(){
        String adminName = "admin";
        System.out.println(adminRepository.existsAdminByName(adminName));
    }

    @Test
    void testExclude(){
        List<Admin> adminList = adminRepository
                .findAllByGroupNameIsNotContaining("sudoers", PageRequest.of(1,10))
                .toList();
        System.out.println(adminList.size());
    }
}
