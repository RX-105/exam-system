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
