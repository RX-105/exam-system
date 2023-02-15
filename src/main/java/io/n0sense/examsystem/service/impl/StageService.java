package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.Stage;
import io.n0sense.examsystem.repository.StageRepository;
import io.n0sense.examsystem.service.IStageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log
public class StageService implements IStageService {
    private final StageRepository stageRepository;

    @Override
    public Page<Stage> findAll(int page, int size) {
        return stageRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public int defineStage(Stage stage) {
        if (stageRepository.existsByNameAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(
                stage.getName(), stage.getStartTime(), stage.getEndTime(), 0L)
                || stage.getStartTime().isAfter(stage.getEndTime())) {
            return Status.ERR_TIME_NOT_ALLOWED;
        } else {
            stageRepository.save(stage);
            return Status.OK;
        }
    }

    @Override
    public int updateTime(Long id, LocalDateTime start, LocalDateTime end) {
        Stage stage = stageRepository.findById(id).orElseThrow();
        if (stageRepository.existsByNameAndEndTimeAfterAndStartTimeBeforeAndStageIdNot(
                stage.getName(), start, end, id)
                || stage.getStartTime().isAfter(stage.getEndTime())) {
            return Status.ERR_TIME_NOT_ALLOWED;
        } else {
            stage.setStartTime(start);
            stage.setEndTime(end);
            stageRepository.save(stage);
            return Status.OK;
        }
    }

    @Override
    public void removeStage(Long id) {
        stageRepository.deleteById(id);
    }
}
