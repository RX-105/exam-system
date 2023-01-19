package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Log;
import org.springframework.data.domain.Page;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface ILogService {
    void recordLogin(HttpServletRequest request);
    Page<Log> getUserLogins(String username, int page, int size);
    Page<Log> getGroupLogin(String groupName, int page, int size);
    Page<Log> queryLogByTimeRange(String username, int page, int size,
                                  LocalDate from, LocalDate to);
}
