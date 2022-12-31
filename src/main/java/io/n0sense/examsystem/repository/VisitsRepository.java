package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Visits;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface VisitsRepository extends JpaRepository<Visits, LocalDate> {
}