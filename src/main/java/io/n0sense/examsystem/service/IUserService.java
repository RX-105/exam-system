package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.User;

import java.util.Optional;

public interface IUserService {
    int register(String username, String password, String ip);
    int login(String username, String password);
    Optional<User> findByName(String username);
}
