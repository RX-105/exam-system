package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Log;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ILogService {
    Page<Log> getUserLogins(String username, int page, int size);
    Page<Log> getGroupLogin(String groupName, int page, int size);
    Page<Log> queryLogByTimeRange(String username, int page, int size,
                                  LocalDate from, LocalDate to);
}
