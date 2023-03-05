package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StageRepository extends JpaRepository<Stage, Long> {
    /**
     * 检查数据库内是否存在名称相同、时间存在交集且ID不同的记录。
     * @param name 阶段名称。
     * @param start 阶段开始时间。
     * @param end 阶段结束时间。
     * @param stageId 阶段ID。用于更新某一个记录时，ID使用原纪录的ID；新增记录时，使用小于100000的任何ID。
     * @return 一个布尔值，用于判断数据库内确实存在满足条件的记录。
     */
//    @Query("""
//            select (count(s) > 0) from Stage s
//            where s.name = ?1
//            and s.endTime > ?2
//            and s.startTime < ?3
//            """)
    boolean existsByNameAndSchoolIdAndEndTimeAfterAndStartTimeBeforeAndStageIdNot
    (String name, Long schoolId, LocalDateTime start, LocalDateTime end, Long stageId);

    List<Stage> findStagesBySchoolId(Long schoolId);
    List<Stage> findAllByNameAndSchoolId(String name, Long schoolId);
}
