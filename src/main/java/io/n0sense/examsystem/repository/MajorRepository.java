package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Long>, BaseRepository<Major> {
}