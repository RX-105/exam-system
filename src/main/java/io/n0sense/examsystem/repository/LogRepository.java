package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface LogRepository extends JpaRepository<Log, Long> {
    // 按照用户名和行为搜索
    Page<Log> findAllByUsernameAndAction(String username, String action, Pageable pageable);
    // 按照组名和行为搜索
    Page<Log> findAllByGroupNameAndAction(String groupName, String action, Pageable pageable);
    // 按照用户名和行为搜索，并指定时间范围
    Page<Log> findAllByUsernameAndActionAndTimeBetween(
            String username, String action, LocalDateTime from, LocalDateTime to, Pageable pageable);
}