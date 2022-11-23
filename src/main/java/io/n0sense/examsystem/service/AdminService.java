package io.n0sense.examsystem.service;

import io.n0sense.examsystem.commons.CommonStatus;
import io.n0sense.examsystem.entity.Admin;
import io.n0sense.examsystem.repository.AdminRepository;
import io.n0sense.examsystem.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public int register(String username, String password, String groupName){
        if (adminRepository.existsAdminByName(username)){
            return CommonStatus.ERR_USERNAME_IN_USE;
        } else {
            Admin admin = new Admin();
            admin.setName(username);
            admin.setPassword(PasswordEncoder.SHA256Encrypt(password));
            admin.setGroupName(groupName);
            adminRepository.save(admin);
            return CommonStatus.OK;
        }
    }

    public int login(String username, String password){
        Optional<Admin> admin = adminRepository.findAdminByName(username);
        if (admin.isPresent()){
            String encodedPassword = PasswordEncoder.SHA256Encrypt(password);
            if (encodedPassword.equals(admin.get().getPassword())){
                return CommonStatus.OK;
            } else {
                return CommonStatus.ERR_INCORRECT_PASSWORD;
            }
        } else {
            return CommonStatus.ERR_USER_NOT_FOUND;
        }
    }
}
