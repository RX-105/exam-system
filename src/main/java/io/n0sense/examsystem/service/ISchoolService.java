package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.School;

import java.util.List;

public interface ISchoolService {
    List<School> findAll();
    School findSchool(Long schoolId);
}
