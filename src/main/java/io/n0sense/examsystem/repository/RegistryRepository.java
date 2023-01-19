package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Registry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistryRepository extends JpaRepository<Registry, Long> {
    boolean existsByUsername(String username);
    Optional<Registry> findByUsername(String username);
}