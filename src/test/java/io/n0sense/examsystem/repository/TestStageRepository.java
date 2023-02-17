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
        boolean result = stageRepository.existsByNameAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(name, start, end, 100000L);
        System.out.println(result);
        assert result;
        // TEST 2
        start = LocalDateTime.parse("2023-02-10T12:00:00");
        end = LocalDateTime.parse("2023-02-24T12:00:00");
        result = stageRepository.existsByNameAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(name, start, end, 100000L);
        System.out.println(result);
        assert result;
        // TEST 3
        start = LocalDateTime.parse("2023-02-14T12:00:00");
        end = LocalDateTime.parse("2023-02-22T12:00:00");
        result = stageRepository.existsByNameAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(name, start, end, 100000L);
        System.out.println(result);
        assert result;
        // TEST 4
        start = LocalDateTime.parse("2023-02-10T12:00:00");
        end = LocalDateTime.parse("2023-02-11T12:00:00");
        result = stageRepository.existsByNameAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(name, start, end, 100000L);
        System.out.println(result);
        assert !result;
        System.out.println(version);
    }
}
