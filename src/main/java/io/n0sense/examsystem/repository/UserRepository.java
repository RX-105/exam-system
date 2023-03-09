package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, BaseRepository<User> {
    List<User> findAllBySchoolId(Long schoolId);
}
