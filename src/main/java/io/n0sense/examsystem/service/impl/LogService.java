package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.entity.Log;
import io.n0sense.examsystem.repository.LogRepository;
import io.n0sense.examsystem.service.ILogService;
import io.n0sense.examsystem.util.IpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService implements ILogService {
    private final LogRepository logRepository;

    /**
     * 添加一条新的登陆记录，登录信息来源自session中的数据。
     * @param request HTTP Servlet请求对象
     */
    @Override
    public void recordLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        Log login = Log.builder()
                .username((String) session.getAttribute("username"))
                .groupName((String) session.getAttribute("group"))
                .ip(IpUtil.getIpAddress(request))
                .time(LocalDateTime.now())
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
        return logRepository.findAllByUsername(username, PageRequest.of(page, size));
    }

    /**
     * 根据组名获取该分组的所有登录记录。
     * @param groupName 查找日志的目标组名
     * @param page 目标页码， 从0开始
     * @param size 每页大小
     * @return 满足组名条件的所有日志分页对象
     */
    public Page<Log> getGroupLogin(String groupName, int page, int size){
        return logRepository.findAllByGroupName(groupName, PageRequest.of(page, size));
    }

    /**
     * 根据用户名在指定时间区间内获取该用户的所有登录记录。开始时间具体为当天00:00:00，结束时间具体为当天23:59:59。
     * @param username 查找日志的目标用户名
     * @param page 目标页码， 从0开始
     * @param size 每页大小
     * @param from 开始日期
     * @param to 结束日期
     * @return 满足用户名条件的所有日志分页对象
     */
    public Page<Log> queryLogByTimeRange(String username, int page, int size,
                                         LocalDate from, LocalDate to){
        logRepository.findAllByUsernameAndTimeBetween(
                username,
                from.atStartOfDay(),
                to.atTime(23, 59, 59),
                PageRequest.of(page, size)
        );
        return null;
    }
}
