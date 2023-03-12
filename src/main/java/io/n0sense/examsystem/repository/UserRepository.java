package io.n0sense.examsystem.repository;

import io.n0sense.examsystem.dto.BasicUserDTO;
import io.n0sense.examsystem.dto.ExamUserDTO;
import io.n0sense.examsystem.entity.School;
import io.n0sense.examsystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, BaseRepository<User> {
    List<BasicUserDTO> findAllBySchool(School school);

    List<BasicUserDTO> findBySchoolAndIsConfirmedTrue(School school);
    Page<ExamUserDTO> findAllBySchoolAndIsConfirmedTrue(School school, Pageable pageable);

    long countBySchool(School school);

    long countBySchoolAndIsConfirmedTrue(School school);

    @Transactional
    @Modifying
    @Query("update User u set u.seatId = ?1, u.roomId = ?2 where u.userId = ?3")
    void updateSeatAndRoom(Long seatId, Long roomId, Long userId);
}
