package io.n0sense.examsystem.dto;

import io.n0sense.examsystem.entity.Major;
import io.n0sense.examsystem.entity.School;

public interface ExamUserDTO {
    Long getUserId();
    String getName();
    String getRealname();
    School getSchool();
    Major getMajor();
    Long getRoomId();
    Long getSeatId();
}
