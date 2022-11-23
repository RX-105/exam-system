package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
}