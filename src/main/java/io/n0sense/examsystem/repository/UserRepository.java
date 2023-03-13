/*
 * This file is part of exam-system.
 *
 * exam-system is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * exam-system is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with exam-system. If not, see <https://www.gnu.org/licenses/>.
 */

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
