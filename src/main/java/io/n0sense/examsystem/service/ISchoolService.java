package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.School;

import java.util.List;
import java.util.Optional;

public interface ISchoolService {
    List<School> findAll();
    Optional<School> findSchool(Long schoolId);
}
