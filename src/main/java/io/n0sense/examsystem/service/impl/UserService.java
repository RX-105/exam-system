package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.Registry;
import io.n0sense.examsystem.entity.User;
import io.n0sense.examsystem.repository.RegistryRepository;
import io.n0sense.examsystem.repository.UserRepository;
import io.n0sense.examsystem.service.IUserService;
import io.n0sense.examsystem.util.IpUtil;
import io.n0sense.examsystem.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RegistryRepository registryRepository;
    private final HttpServletRequest request;
    /**
     * 添加一个新用户(学生)，并添加注册表项。
     * @param username 用户名
     * @param password 用户密码
     * @return <code>CommonStatus</code>下定义的状态值，可能取值有<code>OK, ERR_USERNAME_IN_USE</code>。
     */
    @Override
    public int register(@NonNull String username, @NonNull String password) {
        // 注册表中记录的用户名可能包含管理员侧的用户名，需要同时检查。
        // 这tm什么鬼设计啊！
        if (userRepository.existsByName(username) ||
                registryRepository.existsByUsername(username)){
            return Status.ERR_USERNAME_IN_USE;
        } else {
            User user = new User();
            user.setName(username);
            user = userRepository.save(user);
            Registry registry = new Registry(
                    user.getUserId(),
                    user.getName(),
                    PasswordEncoder.SHA256Encrypt(password),
                    IpUtil.getIpAddress(request),
                    LocalDateTime.now()
            );
            registryRepository.save(registry);
            return Status.OK;
        }
    }

    /**
     * 检查给定的信息是否匹配。
     * @param username 用户名
     * @param password 用户密码
     * @return <code>CommonStatus</code>下定义的状态值，可能取值有<code>OK, ERR_INCORRECT_PASSWORD, ERR_USER_NOT_FOUNT</code>。
     */
    @Override
    public int login(@NonNull String username, @NonNull String password) {
        if (registryRepository.existsByUsername(username)){
            Registry registry = registryRepository.findByUsername(username).orElseThrow();
            String encryptedPassword = PasswordEncoder.SHA256Encrypt(password);
            if (encryptedPassword.equals(registry.getPassword())){
                return Status.OK;
            } else {
                return Status.ERR_INCORRECT_PASSWORD;
            }
        } else {
            return Status.ERR_USER_NOT_FOUND;
        }
    }

    @Override
    public Optional<User> findByName(String username) {
        return userRepository.findByName(username);
    }
}
