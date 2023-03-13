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
