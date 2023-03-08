package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.entity.Exam;
import io.n0sense.examsystem.repository.ExamRepository;
import io.n0sense.examsystem.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService implements IExamService {
    private final ExamRepository examRepository;

    @Override
    public Exam addExam(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public Page<Exam> findAllByMajorId(Long majorId, int page, int size) {
        return examRepository.findAllByMajorId(majorId, PageRequest.of(page, size));
    }
}