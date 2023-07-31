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

package io.n0sense.examsystem.service;

import io.n0sense.examsystem.entity.Log;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ILogService {
    void record(String action, String message, HttpServletRequest request);

    Page<Log> getUserLogins(String username, int page, int size);
    Page<Log> getGroupLogin(String groupName, int page, int size);
    Page<Log> queryLogByTimeRange(String username, String action, int page, int size,
                                  LocalDate from, LocalDate to);
}
