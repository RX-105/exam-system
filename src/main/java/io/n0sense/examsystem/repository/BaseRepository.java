package io.n0sense.examsystem.repository;

import java.util.Optional;

public interface BaseRepository<T> {
    boolean existsByName(String name);

    Optional<T> findByName(String name);
}