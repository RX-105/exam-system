package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Major;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IMajorService {
    Major addMajor(Major major);

    boolean isDuplicate(String name, Long schoolId);
    Page<Major> findAll(int page, int size);
    List<Major> findAllBySchoolId(Long schoolId);
}
