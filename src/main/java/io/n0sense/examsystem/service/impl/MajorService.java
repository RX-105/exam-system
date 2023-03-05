package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.entity.Major;
import io.n0sense.examsystem.repository.MajorRepository;
import io.n0sense.examsystem.service.IMajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService implements IMajorService {
    private final MajorRepository majorRepository;
    @Override
    public Major addMajor(Major major) {
        return majorRepository.save(major);
    }

    @Override
    public boolean isDuplicate(String name, Long schoolId) {
        return majorRepository.existsByNameAndSchoolId(name, schoolId);
    }

    @Override
    public Page<Major> findAll(int page, int size) {
        return majorRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Major> findAllBySchoolId(Long schoolId) {
        return majorRepository.findAllBySchoolId(schoolId);
    }
}
