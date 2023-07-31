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

package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.entity.Log;
import io.n0sense.examsystem.repository.LogRepository;
import io.n0sense.examsystem.service.ILogService;
import io.n0sense.examsystem.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService implements ILogService {
    private final LogRepository logRepository;

    /**
     * 新增一条日志记录。
     * @param action 日志记录的行为类别，参考{@link io.n0sense.examsystem.commons.constants.Actions Actions}
     * @param message 日志记录的额外信息
     * @param request 即HttpServletRequest对象，用于获取用户名、用户组和IP
     */
    @Override
    public void record(String action, String message, HttpServletRequest request){
        HttpSession session = request.getSession();
        Log login = Log.builder()
                .username((String) session.getAttribute("username"))
                .groupName((String) session.getAttribute("group"))
                .ip(IpUtil.getIpAddress(request))
                .time(LocalDateTime.now())
                .action(action)
                .extras(message)
                .build();
        logRepository.save(login);
    }

    /**
     * 根据用户名获取该用户的所有登录记录。
     * @param username 查找日志的目标用户名
     * @param page 目标页码， 从0开始
     * @param size 每页大小
     * @return 满足用户名条件的所有日志分页对象
     */
    public Page<Log> getUserLogins(String username, int page, int size){
        return logRepository.findAllByUsernameAndAction(
                username, "login", PageRequest.of(page, size)
        );
    }

    /**
     * 根据组名获取该分组的所有登录记录。
     * @param groupName 查找日志的目标组名
     * @param page 目标页码， 从0开始
     * @param size 每页大小
     * @return 满足组名条件的所有日志分页对象
     */
    public Page<Log> getGroupLogin(String groupName, int page, int size){
        return logRepository.findAllByGroupNameAndAction(
                groupName, "login", PageRequest.of(page, size)
        );
    }

    /**
     * 根据用户名在指定时间区间内获取该用户的所有登录记录。开始时间具体为当天00:00:00，结束时间具体为当天23:59:59。
     * @param username 查找日志的目标用户名
     * @param action 过滤用户的行为类型
     * @param page 目标页码， 从0开始
     * @param size 每页大小
     * @param from 开始日期
     * @param to 结束日期
     * @return 满足用户名条件的所有日志分页对象
     */
    public Page<Log> queryLogByTimeRange(String username, String action, int page, int size,
                                         LocalDate from, LocalDate to){
        return logRepository.findAllByUsernameAndActionAndTimeBetween(
                username,
                "login",
                from.atStartOfDay(),
                to.atTime(23, 59, 59),
                PageRequest.of(page, size)
        );
    }
}
