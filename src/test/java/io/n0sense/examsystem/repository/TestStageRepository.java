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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class TestStageRepository {
    @Autowired
    StageRepository stageRepository;
    @Value("${application.version}")
    String version;
    @Test
    void testExists() {
        // TEST 1
        String name = "招考信息发布";
        LocalDateTime start = LocalDateTime.parse("2023-02-14T12:00:00");
        LocalDateTime end = LocalDateTime.parse("2023-03-01T12:00:00");
        boolean result = stageRepository.existsByNameAndSchoolIdAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(name, 100001L, start, end, 100000L);
        System.out.println(result);
        assert result;
        // TEST 2
        start = LocalDateTime.parse("2023-02-10T12:00:00");
        end = LocalDateTime.parse("2023-02-24T12:00:00");
        result = stageRepository.existsByNameAndSchoolIdAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(name, 100001L, start, end, 100000L);
        System.out.println(result);
        assert result;
        // TEST 3
        start = LocalDateTime.parse("2023-02-14T12:00:00");
        end = LocalDateTime.parse("2023-02-22T12:00:00");
        result = stageRepository.existsByNameAndSchoolIdAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(name, 100001L, start, end, 100000L);
        System.out.println(result);
        assert result;
        // TEST 4
        start = LocalDateTime.parse("2023-02-10T12:00:00");
        end = LocalDateTime.parse("2023-02-11T12:00:00");
        result = stageRepository.existsByNameAndSchoolIdAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(name, 100001L, start, end, 100000L);
        System.out.println(result);
        assert !result;
        System.out.println(version);
    }
}
