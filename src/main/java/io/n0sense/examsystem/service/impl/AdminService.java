package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.commons.CommonStatus;
import io.n0sense.examsystem.entity.*;
import io.n0sense.examsystem.repository.*;
import io.n0sense.examsystem.service.IAdminService;
import io.n0sense.examsystem.util.IpUtil;
import io.n0sense.examsystem.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * 实现管理员用户服务的类。
 * @author kazuha
 * @since 2022/11/23
 */
@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final AdminRepository adminRepository;
    private final RegistryRepository registryRepository;
    private final MajorRepository majorRepository;
    private final ExamRepository examRepository;
    private final LogRepository logRepository;

    /**
     * 添加一个新用户，并添加注册表项。
     * @param username 用户名
     * @param password 用户密码
     * @param groupName 组名
     * @param ip IP地址
     * @return <code>CommonStatus</code>下定义的状态值，可能取值有<code>OK, ERR_USERNAME_IN_USE</code>。
     */
    @Override
    public int register(String username, String password, String groupName, String ip){
        if (adminRepository.existsAdminByName(username)){
            return CommonStatus.ERR_USERNAME_IN_USE;
        } else {
            Admin admin = new Admin();
            admin.setName(username);
            admin.setPassword(PasswordEncoder.SHA256Encrypt(password));
            admin.setGroupName(groupName);
            adminRepository.save(admin);

            Optional<Admin> savedAdmin = adminRepository.findAdminByName(username);
            if (savedAdmin.isPresent()){
                admin = savedAdmin.get();
                Registry registry = new Registry(
                        admin.getAdminId(), admin.getName(), admin.getPassword(), ip,
                        LocalDateTime.now());
                registryRepository.save(registry);
            }
            return CommonStatus.OK;
        }
    }

    /**
     * 检查给定的信息是否匹配。
     * @param username 用户名
     * @param password 用户密码
     * @return <code>CommonStatus</code>下定义的状态值，可能取值有<code>OK, ERR_INCORRECT_PASSWORD, ERR_USER_NOT_FOUNT</code>。
     */
    @Override
    public int login(String username, String password){
        Optional<Admin> admin = adminRepository.findAdminByName(username);
        if (admin.isPresent()){
            String encodedPassword = PasswordEncoder.SHA256Encrypt(password);
            if (encodedPassword.equals(admin.get().getPassword())){
                return CommonStatus.OK;
            } else {
                return CommonStatus.ERR_INCORRECT_PASSWORD;
            }
        } else {
            return CommonStatus.ERR_USER_NOT_FOUND;
        }
    }

    @Override
    public int addEnrollmentInfo(String majorName, int applicant, int enrollment, int admission,
                                 double score, String examName, LocalDateTime start,
                                 LocalDateTime end) {
        if (this.majorRepository.existsByName(majorName)) {
            return CommonStatus.ERR_MAJOR_EXISTS;
        } else if (this.examRepository.existsByName(examName)) {
            return CommonStatus.ERR_EXAM_EXISTS;
        } else {
            Major major = new Major(0L, majorName, applicant, enrollment, score, admission);
            major = this.majorRepository.save(major);
            Exam exam = new Exam(0L, examName, major.getId(), start, end);
            this.examRepository.save(exam);
            return CommonStatus.OK;
        }
    }

    @Override
    public ResponseEntity getEnrollmentInfo(String majorName) {
        ResponseEntity responseEntity = new ResponseEntity();
        Optional<Major> optionalMajor = this.majorRepository.findByName(majorName);
        Map<String, Object> data = new HashMap<>();
        if (optionalMajor.isPresent()) {
            responseEntity.setCode(0);
            List<Exam> examList = this.examRepository.findAll();
            data.put("exam", examList);
            responseEntity.setData(data);
        } else {
            responseEntity.setCode(1005);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity getEnrollmentInfo() {
        ResponseEntity responseEntity = new ResponseEntity();
        Map<String, Object> data = new HashMap<>();
        List<Major> majorList = this.majorRepository.findAll();
        List<EnrollmentInfo> enrollmentInfoList = new ArrayList<>();
        if (majorList.size() == 0) {
            responseEntity.setCode(CommonStatus.ERR_MAJOR_NOT_FOUND);
        } else {
            Iterator<Major> var5 = majorList.iterator();

            while(var5.hasNext()) {
                Major major = var5.next();
                List<Exam> examList = this.examRepository.findAll();
                enrollmentInfoList.add(new EnrollmentInfo(major, examList));
            }

            data.put("info", enrollmentInfoList);
            responseEntity.setCode(CommonStatus.OK);
            responseEntity.setData(data);
        }

        return responseEntity;
    }

    @Override
    public Optional<Admin> findByName(String name) {
        return this.adminRepository.findByName(name);
    }


    /**
     * 添加一条新的登陆记录，登录信息来源自session中的数据。
     * @param request HTTP Servlet请求对象
     */
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
