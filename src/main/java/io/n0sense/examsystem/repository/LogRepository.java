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

import io.n0sense.examsystem.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface LogRepository extends JpaRepository<Log, Long> {
    // 按照用户名和行为搜索
    Page<Log> findAllByUsernameAndAction(String username, String action, Pageable pageable);
    // 按照组名和行为搜索
    Page<Log> findAllByGroupNameAndAction(String groupName, String action, Pageable pageable);
    // 按照用户名和行为搜索，并指定时间范围
    Page<Log> findAllByUsernameAndActionAndTimeBetween(
            String username, String action, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
