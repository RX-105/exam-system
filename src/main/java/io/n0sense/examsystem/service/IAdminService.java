package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Admin;
import io.n0sense.examsystem.entity.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IAdminService {
    int register(String username, String password, String groupName, String ip);
    int login(String username, String password);
    int addEnrollmentInfo(String majorName, int applicant, int enrollment, int admission,
                          double score, String examName, LocalDateTime start, LocalDateTime end);
    ResponseEntity getEnrollmentInfo(String majorName);
    ResponseEntity getEnrollmentInfo();
    Optional<Admin> findByName(String name);
}
