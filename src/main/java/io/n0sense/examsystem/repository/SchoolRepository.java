package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}