package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Long>, BaseRepository<Major> {
    boolean existsByNameAndSchoolId(String name, Long schoolId);
    List<Major> findAllBySchoolId(Long schoolId);
}
