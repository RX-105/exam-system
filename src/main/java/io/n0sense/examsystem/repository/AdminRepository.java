package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>, BaseRepository<Admin> {
    boolean existsAdminByName(String name);
    Optional<Admin> findAdminByName(String name);
}
