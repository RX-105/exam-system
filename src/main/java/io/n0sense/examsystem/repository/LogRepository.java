package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findAllByUsername(String username, Pageable pageable);
    Page<Log> findAllByGroupName(String groupName, Pageable pageable);
    Page<Log> findAllByUsernameAndTimeBetween(
            String username, LocalDateTime from, LocalDateTime to, Pageable pageable);
}