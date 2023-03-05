package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Stage;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IStageService {

    Page<Stage> findAll(int page, int size);
    int defineStage(Stage stage);
    int updateTime(Long id, LocalDateTime start, LocalDateTime end);
    void removeStage(Long id);
    List<Stage> findAllStageBySchoolId(Long schoolId);
    List<Stage> findAllStageBySchoolIdAndName(Long schoolId, String name);
}
