package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>, BaseRepository<Admin> {
    boolean existsAdminByName(String name);
    Optional<Admin> findAdminByName(String name);
    Page<Admin> findAllByGroupNameIsNotContaining(String groupName, Pageable pageable);
}
