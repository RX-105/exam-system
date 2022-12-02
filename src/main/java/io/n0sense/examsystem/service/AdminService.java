package io.n0sense.examsystem.service;

import io.n0sense.examsystem.commons.CommonStatus;
import io.n0sense.examsystem.entity.*;
import io.n0sense.examsystem.repository.AdminRepository;
import io.n0sense.examsystem.repository.ExamRepository;
import io.n0sense.examsystem.repository.MajorRepository;
import io.n0sense.examsystem.repository.RegistryRepository;
import io.n0sense.examsystem.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 实现管理员用户服务的类。
 * @author kazuha
 * @since 2022/11/23
 */
@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final RegistryRepository registryRepository;
    private final MajorRepository majorRepository;
    private final ExamRepository examRepository;

    /**
     * 添加一个新用户，并添加注册表项。
     * @param username 用户名
     * @param password 用户密码
     * @param groupName 组名
     * @param ip IP地址
     * @return <code>CommonStatus</code>下定义的状态值，可能取值有<code>OK, ERR_USERNAME_IN_USE</code>。
     */
    public int register(String username, String password, String groupName, String ip){
        if (adminRepository.existsAdminByName(username)){
            return CommonStatus.ERR_USERNAME_IN_USE;
        } else {
            Admin admin = new Admin();
            admin.setName(username);
            admin.setPassword(PasswordEncoder.SHA256Encrypt(password));
            admin.setGroupName(groupName);
            adminRepository.save(admin);

            Optional<Admin> savedAdmin = adminRepository.findAdminByName(username);
            if (savedAdmin.isPresent()){
                admin = savedAdmin.get();
                Registry registry = new Registry(
                        admin.getAdminId(), admin.getName(), admin.getPassword(), ip,
                        LocalDateTime.now());
                registryRepository.save(registry);
            }
            return CommonStatus.OK;
        }
    }

    /**
     * 检查给定的信息是否匹配。
     * @param username 用户名
     * @param password 用户密码
     * @return <code>CommonStatus</code>下定义的状态值，可能取值有<code>OK, ERR_INCORRECT_PASSWORD, ERR_USER_NOT_FOUNT</code>。
     */
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

    public int addEnrollmentInfo(String majorName, int applicant, int enrollment, int admission, double score, String examName, LocalDateTime start, LocalDateTime end) {
        if (this.majorRepository.existsByName(majorName)) {
            return 1004;
        } else if (this.examRepository.existsByName(examName)) {
            return 1006;
        } else {
            Major major = new Major(0L, majorName, applicant, enrollment, score, admission);
            major = this.majorRepository.save(major);
            Exam exam = new Exam(0L, examName, major.getId(), start, end);
            this.examRepository.save(exam);
            return 0;
        }
    }

    public ResponseEntity getEnrollmentInfo(String majorName) {
        ResponseEntity responseEntity = new ResponseEntity();
        Optional<Major> optionalMajor = this.majorRepository.findByName(majorName);
        Map<String, Object> data = new HashMap<>();
        if (optionalMajor.isPresent()) {
            responseEntity.setCode(0);
            List<Exam> examList = this.examRepository.findAll();
            data.put("exam", examList);
            responseEntity.setData(data);
        } else {
            responseEntity.setCode(1005);
        }

        return responseEntity;
    }

    public ResponseEntity getEnrollmentInfo() {
        ResponseEntity responseEntity = new ResponseEntity();
        Map<String, Object> data = new HashMap<>();
        List<Major> majorList = this.majorRepository.findAll();
        List<EnrollmentInfo> enrollmentInfoList = new ArrayList<>();
        if (majorList.size() == 0) {
            responseEntity.setCode(CommonStatus.ERR_MAJOR_NOT_FOUND);
        } else {
            Iterator<Major> var5 = majorList.iterator();

            while(var5.hasNext()) {
                Major major = var5.next();
                List<Exam> examList = this.examRepository.findAll();
                enrollmentInfoList.add(new EnrollmentInfo(major, examList));
            }

            data.put("info", enrollmentInfoList);
            responseEntity.setCode(CommonStatus.OK);
            responseEntity.setData(data);
        }

        return responseEntity;
    }

    public Optional<Admin> findByName(String name) {
        return this.adminRepository.findByName(name);
    }
}
