package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.entity.School;
import io.n0sense.examsystem.repository.SchoolRepository;
import io.n0sense.examsystem.service.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolService implements ISchoolService {
    private final SchoolRepository schoolRepository;

    @Override
    public List<School> findAll() {
        return schoolRepository.findAll();
    }

    @Override
    public Optional<School> findSchool(Long schoolId) {
        return schoolRepository.findById(schoolId);
    }
}
