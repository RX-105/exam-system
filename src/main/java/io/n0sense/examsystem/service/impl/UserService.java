/*
 * This file is part of exam-system.
 *
 * exam-system is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * exam-system is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with exam-system. If not, see <https://www.gnu.org/licenses/>.
 */

package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.dto.BasicUserDTO;
import io.n0sense.examsystem.entity.Registry;
import io.n0sense.examsystem.entity.School;
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
import java.util.List;
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

    @Override
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    @Override
    public List<BasicUserDTO> findAllBySchoolId(Long schoolId) {
        return userRepository.findAllBySchool(School.builder().schoolId(schoolId).build());
    }

    /**
     * 检查给定用户名的用户是否存在。
     * @param username 需要检查的用户名
     * @return 如果存在，则返回true，否则返回false
     */
    @Override
    public boolean checkExistence(String username) {
        return userRepository.existsByName(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
