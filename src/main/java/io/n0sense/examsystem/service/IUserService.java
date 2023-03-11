package io.n0sense.examsystem.service;

import io.n0sense.examsystem.dto.BasicUserDTO;
import io.n0sense.examsystem.entity.User;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    int register(@NonNull String username, @NonNull String password);
    int login(String username, String password);
    Optional<User> findByName(String username);

    Optional<User> findById(Long id);
    List<BasicUserDTO> findAllBySchoolId(Long schoolId);

    boolean checkExistence(String username);

    User save(User user);
}
