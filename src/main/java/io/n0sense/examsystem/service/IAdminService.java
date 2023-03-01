package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Admin;
import io.n0sense.examsystem.entity.R;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IAdminService {

    int register(String username, String password, String groupName, Long schoolId);

    int login(String username, String password);
    int addEnrollmentInfo(String majorName, int applicant, int enrollment, int admission,
                          double score, String examName, LocalDateTime start, LocalDateTime end);
    R getEnrollmentInfo(String majorName);
    R getEnrollmentInfo();
    Optional<Admin> findByName(String name);
    Page<Admin> getAllAdmins(int page, int size);

    Page<Admin> getAllAdmins(String excludeGroupName, int page, int size);

    boolean resetPassword(Long id, HttpServletRequest request);

    void dropUser(Long id);

    boolean checkExistence(String username);
}
