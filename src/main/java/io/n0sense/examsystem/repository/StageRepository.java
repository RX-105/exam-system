package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, Long> {
}