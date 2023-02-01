package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Backup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<Backup, Long> {
}