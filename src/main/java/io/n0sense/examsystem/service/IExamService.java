package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Exam;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IExamService {
    Exam addExam(Exam exam);
    Page<Exam> findAllByMajorId(Long majorId, int page, int size);

    List<Exam> findAllByMajorId(Long majorId);
}
