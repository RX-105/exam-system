package io.n0sense.examsystem.service;

public interface IUserService {
    int register(String username, String password, String ip);
    int login(String username, String password);
}
