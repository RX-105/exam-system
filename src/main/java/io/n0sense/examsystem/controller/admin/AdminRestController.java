package io.n0sense.examsystem.controller.admin;

import io.n0sense.examsystem.annotation.NoNullArgs;
import io.n0sense.examsystem.annotation.RecordLog;
import io.n0sense.examsystem.commons.constants.Actions;
import io.n0sense.examsystem.commons.constants.Identities;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.Admin;
import io.n0sense.examsystem.entity.Log;
import io.n0sense.examsystem.entity.R;
import io.n0sense.examsystem.entity.Stage;
import io.n0sense.examsystem.service.impl.AdminService;
import io.n0sense.examsystem.service.impl.BackupService;
import io.n0sense.examsystem.service.impl.LogService;
import io.n0sense.examsystem.service.impl.StageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 管理员页面中的REST API控制器。
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {
    private final AdminService adminService;
    private final LogService logService;
    private final BackupService backupService;
    private final StageService stageService;
    private final Logger log = LoggerFactory.getLogger(AdminRestController.class);

    @ExceptionHandler(NullPointerException.class)
    public R nullParameterHandler() {
        return R.builder()
                .status(Status.ERR_PARAMETER_NOT_PRESENT)
                .message("参数不完整。")
                .data(Map.of("location", "/404"))
                .build();
    }

    @PostMapping({"/register"})
    @RecordLog(action = {Actions.REGISTER, Actions.LOGIN}, message = {"主动注册"})
    @NoNullArgs
    public R register(String username, String password, String groupName, Long schoolId, HttpServletRequest request) {
        // TODO: 2023/2/15 注册新增一个参数，前端要改
        int result = this.adminService.register(username, password, groupName, schoolId);
        if (result == Status.OK) {
            // 检查判断为注册成功
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("group", groupName);
            request.getSession().setAttribute("role", Identities.ROLE_ADMIN.getKey());

            Map<String, Object> data = new HashMap<>();
            data.put("location", "/" + Identities.ROLE_ADMIN.getKey() + "/home");
            return R.builder()
                    .status(result)
                    .message("注册成功。")
                    .data(data)
                    .build();
        } else if (result == Status.ERR_USERNAME_IN_USE) {
            // 用户密码已经存在
            return R.builder()
                    .status(result)
                    .message("用户名已占用。")
                    .build();
        } else {
            // 其他错误
            this.log.error("register: 预期外的错误  " + result);
            return R.builder()
                    .status(Status.ERR_UNSPECIFIED)
                    .message("发生未知错误。")
                    .build();
        }
    }

    @PostMapping({"/register2"})
    @RecordLog(action = Actions.REGISTER, message = "为他人注册")
    @NoNullArgs
    public R register(String username, String password, String groupName, Long schoolId, HttpSession session) {
        int result = this.adminService.register(username, password, groupName, schoolId);
        if (result == Status.OK) {
            log.info(session.getAttribute("username") + "为" + username + "创建了用户");
            // 检查判断为注册成功
            return R.builder()
                    .status(result)
                    .message("用户注册成功。")
                    .build();
        } else if (result == Status.ERR_USERNAME_IN_USE) {
            // 用户密码已经存在
            return R.builder()
                    .status(result)
                    .message("用户名已占用。")
                    .build();
        } else {
            // 其他错误
            this.log.error("register: Unresolved result " + result);
            return R.builder()
                    .status(Status.ERR_UNSPECIFIED)
                    .message("发生未知错误。")
                    .build();
        }
    }

    @PostMapping({"/login"})
    @RecordLog(action = Actions.LOGIN)
    @NoNullArgs
    public R login(String username, String password, Boolean remember, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        int result = this.adminService.login(username, password);
        // 通过login方法的话，admin一定不是null，不用担心这里的isPresent()检查
        Admin admin = this.adminService.findByName(username).orElse(new Admin());
        if (result == 0) {
            // 判断为信息正确
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("group", admin.getGroupName());
            request.getSession().setAttribute("role", Identities.ROLE_ADMIN.getKey());
            if (remember) {
                request.getSession().setAttribute("remember", true);
            }

            data.put("location", "/" + Identities.ROLE_ADMIN.getKey() + "/home");
            return R.builder()
                    .status(result)
                    .message("登陆成功。")
                    .data(data)
                    .build();
        } else if (result == Status.ERR_INCORRECT_PASSWORD
                || result == Status.ERR_USER_NOT_FOUND) {
            // 判断为密码错误或用户不存在，但是只能提示密码错误
            return R.builder()
                    .status(Status.ERR_INCORRECT_PASSWORD)
                    .message("密码错误。")
                    .build();
        } else {
            // 其他错误
            this.log.error("login: 预期外的错误  " + result);
            return R.builder()
                    .status(Status.ERR_UNSPECIFIED)
                    .message("发生未知错误。")
                    .build();
        }
    }

    @PostMapping({"/publish"})
    public ModelAndView publishEnrollmentInfo(String majorName, int applicant, int enrollment, int admission, double score, String examName, LocalDateTime start, LocalDateTime end, Model model) {
        int result = this.adminService.addEnrollmentInfo(majorName, applicant, enrollment, admission, score, examName, start, end);
        if (result == Status.ERR_MAJOR_EXISTS) {
            model.addAttribute("msg", "已经有这个专业了。");
            return new ModelAndView("/" + Identities.ROLE_ADMIN.getKey() +
                    "/" + Identities.GROUP_RECRUIT_AFFAIRS.getKey() + "/recruit-maintain");
        } else if (result == Status.ERR_EXAM_EXISTS) {
            model.addAttribute("msg", "已经有这项考试了。");
            return new ModelAndView("/" + Identities.ROLE_ADMIN.getKey() +
                    "/" + Identities.GROUP_RECRUIT_AFFAIRS.getKey() + "/recruit-maintain");
        } else if (result == Status.OK) {
            model.addAttribute("msg", "添加完成。");
            return new ModelAndView("/" + Identities.ROLE_ADMIN.getKey() +
                    "/" + Identities.GROUP_RECRUIT_AFFAIRS.getKey() + "/recruit-maintain");
        } else {
            this.log.error("publishEnrollmentInfo: Unresolved result " + result);
            return new ModelAndView("404");
        }
    }

    @PostMapping("/queryLoginByTime")
    public ModelAndView queryLoginByTimeRange(String username, LocalDate from, LocalDate to, Integer page, Model model) {
        Page<Log> logPage = logService.queryLogByTimeRange(
                username,
                (page == null ? 0 : page),
                10,
                from,
                to
        );
        List<Log> logList = logPage.toList();

        model.addAttribute("loginsPage", logPage);
        model.addAttribute("logins", logList);

        return new ModelAndView("/admin/login-history");
    }

    @PostMapping("/resetPassword")
    @NoNullArgs
    public R resetPassword(Long id) {
        if (adminService.resetPassword(id)) {
            return R.builder()
                    .status(Status.OK)
                    .message("ID为" + id + "的用户的密码重置成功，新密码是1234。")
                    .build();
        } else {
            return R.builder()
                    .status(Status.ERR_OPERATION_FAILED)
                    .message("未能重置ID为" + id + "的用户的密码。")
                    .build();
        }
    }

    @PostMapping("/dropUser")
    @NoNullArgs
    public R dropUser(Long id) {
        adminService.dropUser(id);
        return R.builder()
                .status(Status.OK)
                .message("ID为" + id + "的用户已经移除。")
                .build();
    }

    @GetMapping("/backup")
    @NoNullArgs
    public R addBackup() {
        try {
            backupService.dumpDatabase();
        } catch (IOException e) {
            return R.builder()
                    .status(Status.ERR_OPERATION_FAILED)
                    .message("无法创建备份。")
                    .build();
        }
        return R.builder()
                .status(Status.OK)
                .message("备份创建成功。")
                .build();
    }

    @PostMapping("/backup/{id}")
    @NoNullArgs
    public R applyBackup(@PathVariable("id") Long id) {
        try {
            backupService.applyFromId(id);
        } catch (FileNotFoundException e) {
            return R.builder()
                    .status(Status.ERR_FILE_NOT_FOUND)
                    .message("备份文件丢失。")
                    .build();
        } catch (IOException e) {
            return R.builder()
                    .status(Status.ERR_OPERATION_FAILED)
                    .message("无法创建备份。")
                    .build();
        } catch (NoSuchElementException e) {
            return R.builder()
                    .status(Status.ERR_NO_SUCH_ELEMENT)
                    .message("该备份不存在。")
                    .build();
        }
        return R.builder()
                .status(Status.OK)
                .message("数据库还原完成。")
                .build();
    }

    @DeleteMapping("/backup/{id}")
    @NoNullArgs
    public R removeBackup(@PathVariable("id") Long id) {
        try {
            backupService.removeBackup(id);
        } catch (FileNotFoundException | NoSuchElementException e) {
            return R.builder()
                    .status(Status.ERR_NO_SUCH_ELEMENT)
                    .message("备份文件可能不存在，但已移除该记录。")
                    .build();
        } catch (IOException e) {
            return R.builder()
                    .status(Status.ERR_OPERATION_FAILED)
                    .message("无法移除备份。")
                    .build();
        }
        return R.builder()
                .status(Status.OK)
                .message("已移除该备份。")
                .build();
    }

    @PostMapping("/stage")
    @NoNullArgs
    public R defineStage(String name, LocalDateTime start, LocalDateTime end, String remarks, HttpSession session) {
        Stage stage = Stage.builder()
                .name(name)
                .startTime(start)
                .endTime(end)
                .remark(remarks)
                .definer((String) session.getAttribute("username"))
                .build();
        int result = stageService.defineStage(stage);
        if (result == Status.OK) {
            return R.builder()
                    .status(result)
                    .message("定义阶段完成。")
                    .build();
        } else if (result == Status.ERR_TIME_NOT_ALLOWED) {
            return R.builder()
                    .status(result)
                    .message("时间段不可用，比如这个名称的阶段已经存在，且时间存在重合。")
                    .build();
        } else {
            log.error("defineStage: 预期外的错误 " + result);
            return R.builder()
                    .status(Status.ERR_UNSPECIFIED)
                    .message("发生未知错误。")
                    .build();
        }
    }

    @PostMapping("/stage/{id}")
    @NoNullArgs
    public R editStageTime(@PathVariable("id") Long id,
                           LocalDateTime start, LocalDateTime end) {
        int result;
        try {
            result = stageService.updateTime(id, start, end);
        } catch (NoSuchElementException e) {
            return R.builder()
                    .status(Status.ERR_NO_SUCH_ELEMENT)
                    .message("请求修改的阶段并不存在。")
                    .build();
        }
        if (result == Status.OK) {
            return R.builder()
                    .status(result)
                    .message("修改完成。")
                    .build();
        } else if (result == Status.ERR_TIME_NOT_ALLOWED) {
            return R.builder()
                    .status(result)
                    .message("时间段不可用，比如这个时间与同阶段的其他时间存在重合。")
                    .build();
        } else {
            log.error("editStageTime: 预期外的错误 " + result);
            return R.builder()
                    .status(Status.ERR_UNSPECIFIED)
                    .message("发生未知错误。")
                    .build();
        }
    }

    @DeleteMapping("/stage/{id}")
    @NoNullArgs
    public R removeStage(@PathVariable("id") Long id) {
        stageService.removeStage(id);
        return R.builder()
                .status(Status.OK)
                .message("已移除这个阶段。")
                .build();
    }
}
