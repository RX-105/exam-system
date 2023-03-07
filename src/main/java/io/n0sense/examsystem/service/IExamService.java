package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Exam;
import org.springframework.data.domain.Page;

public interface IExamService {
    Exam addExam(Exam exam);
    Page<Exam> findAllByMajorId(Long majorId, int page, int size);
}
