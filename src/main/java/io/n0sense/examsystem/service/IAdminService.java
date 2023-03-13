package io.n0sense.examsystem.service;

import io.n0sense.examsystem.dto.ExamUserDTO;
import io.n0sense.examsystem.entity.Admin;
import io.n0sense.examsystem.entity.R;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface IAdminService {

    int register(String username, String password, String groupName, Long schoolId);

    int login(String username, String password);
    Optional<Admin> findByName(String name);

    Optional<Admin> findById(Long id);

    Page<Admin> getAllAdmins(int page, int size);

    Page<Admin> getAllAdmins(String excludeGroupName, int page, int size);

    boolean resetPassword(Long id, HttpServletRequest request);

    void dropUser(Long id);

    boolean checkExistence(String username);
    void studentConfirm(Long uid);
    long[] getConfirmData(Long schoolId);
    void assignTickets(Long schoolId);

    Page<ExamUserDTO> getExamUserInfo(Long schoolId, int page, int size);

    Optional<Resource> exportExamUserInfo(Long schoolId) throws IOException;
}
