package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.commons.constants.Identities;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.*;
import io.n0sense.examsystem.repository.*;
import io.n0sense.examsystem.service.IAdminService;
import io.n0sense.examsystem.util.IpUtil;
import io.n0sense.examsystem.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 实现管理员用户服务的类。
 * @author kazuha
 * @since 2022/11/23
 */
@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final AdminRepository adminRepository;
    private final RegistryRepository registryRepository;
    private final MajorRepository majorRepository;
    private final ExamRepository examRepository;
    private final HttpServletRequest request;

    /**
     * 添加一个新用户，并添加注册表项。
     * @param username 用户名
     * @param password 用户密码
     * @param groupName 组名
     * @return <code>CommonStatus</code>下定义的状态值，可能取值有<code>OK, ERR_USERNAME_IN_USE</code>。
     */
    @Override
    public int register(String username, String password, String groupName, Long schoolId){
        if ((username.equals(Identities.GROUP_SYSTEM_ADMIN.getKey()) && schoolId != 100000L) ||
                (!username.equals(Identities.GROUP_SYSTEM_ADMIN.getKey()) && schoolId == 100000L)) {
            return Status.ERR_PARAMETER_NOT_MATCH;
        }
        else if (adminRepository.existsAdminByName(username)){
            return Status.ERR_USERNAME_IN_USE;
        } else {
            Admin admin = adminRepository.save(
                    Admin.builder()
                    .name(username)
                    .password(PasswordEncoder.SHA256Encrypt(password))
                    .groupName(groupName)
                    .schoolId(schoolId)
                    .build()
            );
            Registry registry = new Registry(
                    admin.getAdminId(),
                    admin.getName(),
                    admin.getPassword(),
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
    public int login(String username, String password){
        Optional<Admin> admin = adminRepository.findAdminByName(username);
        if (admin.isPresent()){
            String encodedPassword = PasswordEncoder.SHA256Encrypt(password);
            if (encodedPassword.equals(admin.get().getPassword())){
                return Status.OK;
            } else {
                return Status.ERR_INCORRECT_PASSWORD;
            }
        } else {
            return Status.ERR_USER_NOT_FOUND;
        }
    }

    @Override
    public int addEnrollmentInfo(String majorName, int applicant, int enrollment, int admission,
                                 double score, String examName, LocalDateTime start,
                                 LocalDateTime end) {
        if (this.majorRepository.existsByName(majorName)) {
            return Status.ERR_MAJOR_EXISTS;
        } else if (this.examRepository.existsByName(examName)) {
            return Status.ERR_EXAM_EXISTS;
        } else {
            Major major = new Major(0L, majorName, 100001L, applicant, enrollment, new BigDecimal(score), admission);
            major = this.majorRepository.save(major);
            Exam exam = new Exam(0L, examName, major.getId(), start, end);
            this.examRepository.save(exam);
            return Status.OK;
        }
    }

    @Override
    public R getEnrollmentInfo(String majorName) {
        R response = new R();
        Optional<Major> optionalMajor = this.majorRepository.findByName(majorName);
        Map<String, Object> data = new HashMap<>();
        if (optionalMajor.isPresent()) {
            response.setStatus(0);
            List<Exam> examList = this.examRepository.findAll();
            data.put("exam", examList);
            response.setData(data);
        } else {
            response.setStatus(1005);
        }

        return response;
    }

    @Override
    public R getEnrollmentInfo() {
        R response = new R();
        Map<String, Object> data = new HashMap<>();
        List<Major> majorList = this.majorRepository.findAll();
        List<EnrollmentInfo> enrollmentInfoList = new ArrayList<>();
        if (majorList.size() == 0) {
            response.setStatus(Status.ERR_MAJOR_NOT_FOUND);
        } else {

            for (Major major : majorList) {
                List<Exam> examList = this.examRepository.findAll();
                enrollmentInfoList.add(new EnrollmentInfo(major, examList));
            }

            data.put("info", enrollmentInfoList);
            response.setStatus(Status.OK);
            response.setData(data);
        }

        return response;
    }

    @Override
    public Optional<Admin> findByName(String name) {
        return this.adminRepository.findByName(name);
    }

    @Override
    public Page<Admin> getAllAdmins(int page, int size) {
        return adminRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Admin> getAllAdmins(String excludeGroupName, int page, int size) {
        return adminRepository.findAllByGroupNameIsNotContaining(excludeGroupName, PageRequest.of(page, size));
    }

    @Override
    public boolean resetPassword(Long id, HttpServletRequest request) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isPresent()){
            Admin admin = optionalAdmin.get();
            admin.setPassword(PasswordEncoder.SHA256Encrypt("1234"));
            adminRepository.save(admin);
            Registry adminRegistry = registryRepository.findById(admin.getAdminId()).orElse(
                    Registry.builder()
                            .userId(admin.getAdminId())
                            .username(admin.getName())
                            .password(admin.getPassword())
                            .ip(IpUtil.getIpAddress(request))
                            .time(LocalDateTime.now())
                            .build()
            );
            adminRegistry.setPassword(admin.getPassword());
            registryRepository.save(adminRegistry);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void dropUser(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public boolean checkExistence(String username) {
        return adminRepository.existsAdminByName(username);
    }
}
