package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.dto.BasicUserDTO;
import io.n0sense.examsystem.dto.ExamUserDTO;
import io.n0sense.examsystem.entity.School;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class TestUserRepository {
    @Autowired
    UserRepository userRepository;

    @Test
    void testCount() {
        System.out.println(userRepository.countBySchoolAndIsConfirmedTrue(School.builder().schoolId(100001L).build()));
    }

    @Test
    void testFindConfirmed() {
        List<BasicUserDTO> userDTOS = userRepository.findBySchoolAndIsConfirmedTrue(School.builder().schoolId(100001L).build());
        System.out.println(userDTOS.get(0).getName());
    }

    @Test
    void testUpdate() {
        userRepository.updateSeatAndRoom(100002L, 32L, 10L);
    }

    @Test
    void testGetExamDTO() {
        Page<ExamUserDTO> examUserDTOS = userRepository.findAllBySchoolIs(School.builder().schoolId(100001L).build(), Pageable.unpaged());
        System.out.println(examUserDTOS.toList().size());
    }
}
