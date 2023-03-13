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
        Page<ExamUserDTO> examUserDTOS = userRepository.findAllBySchoolAndIsConfirmedTrue(School.builder().schoolId(100001L).build(), Pageable.unpaged());
        System.out.println(examUserDTOS.toList().size());
    }
}
