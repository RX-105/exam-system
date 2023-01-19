package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, BaseRepository<User> {
}
