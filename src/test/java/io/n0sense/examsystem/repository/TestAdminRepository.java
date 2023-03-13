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
        Admin admin = new Admin(1002L, "demo2", "1234", "sudoers", 100000L);
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
