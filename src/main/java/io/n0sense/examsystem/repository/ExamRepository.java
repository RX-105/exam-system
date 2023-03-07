package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long>, BaseRepository<Exam> {
    Page<Exam> findAllByMajorId(Long majorId, Pageable pageable);
}
