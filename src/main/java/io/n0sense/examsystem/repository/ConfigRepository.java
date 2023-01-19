package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Long> {
}