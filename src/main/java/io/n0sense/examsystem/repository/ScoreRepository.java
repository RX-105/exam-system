package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}