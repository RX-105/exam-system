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

import io.n0sense.examsystem.entity.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
public class TestLogRepository {
    @Autowired
    LogRepository logRepository;

    @Test
    void testGetAllLogins() {
        System.out.println("By username="
                + logRepository.findAllByUsernameAndAction("aca", "login", Pageable.ofSize(15)).toList().size());
        System.out.println("By groupName="
                + logRepository.findAllByGroupNameAndAction("aca-aff", "login", PageRequest.of(0, 15)).toList().size());
    }

    @Test
    void testGetByTimeRange(){
        Page<Log> logPage = logRepository.findAllByUsernameAndActionAndTimeBetween(
                "aca",
                "login",
                LocalDate.parse("2023-01-18").atStartOfDay(),
                LocalDate.parse("2023-01-19").atStartOfDay(),
                PageRequest.of(0, 10)
        );
        System.out.println(logPage.toList().size());
    }
}
