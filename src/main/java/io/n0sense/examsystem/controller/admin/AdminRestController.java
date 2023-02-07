package io.n0sense.examsystem.controller.admin;

import io.n0sense.examsystem.annotation.NoNullArgs;
import io.n0sense.examsystem.annotation.RecordLog;
import io.n0sense.examsystem.commons.constants.Actions;
import io.n0sense.examsystem.commons.constants.Identities;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.Admin;
import io.n0sense.examsystem.entity.Log;
import io.n0sense.examsystem.entity.R;
import io.n0sense.examsystem.service.impl.AdminService;
import io.n0sense.examsystem.service.impl.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员页面中的REST API控制器。
 */
@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {
    private final AdminService adminService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(AdminRestController.class);

    @PostMapping({"/register"})
    @ResponseBody
    @RecordLog(action = Actions.LOGIN)
    @NoNullArgs
    public R register(String username, String password, String groupName, HttpServletRequest request) {
        int result = this.adminService.register(username, password, groupName);
        Map<String, Object> data = new HashMap<>();
        if (result == Status.OK) {
            // 检查判断为注册成功
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("group", groupName);
            request.getSession().setAttribute("role", Identities.ROLE_ADMIN);

            data.put("location", "/" + Identities.ROLE_ADMIN + "/home");
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
            this.logger.error("register: Unresolved result " + result);
            data.put("location", "/404");
            return R.builder()
                    .status(Status.ERR_UNSPECIFIED)
                    .message("register: Unresolved result " + result)
                    .data(data)
                    .build();
        }
    }

    @PostMapping({"/register2"})
    @ResponseBody
    @NoNullArgs
    public R register(String username, String password, String groupName) {
        int result = this.adminService.register(username, password, groupName);
        if (result == Status.OK) {
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
            this.logger.error("register: Unresolved result " + result);
            return R.builder()
                    .status(Status.ERR_UNSPECIFIED)
                    .message("发生未知错误。")
                    .build();
        }
    }

    @PostMapping({"/login"})
    @ResponseBody
    @RecordLog(action = Actions.LOGIN)
    @NoNullArgs
    public R login(String username, String password, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        int result = this.adminService.login(username, password);
        // 通过login方法的话，admin一定不是null，不用担心这里的isPresent()检查
        Admin admin = this.adminService.findByName(username).orElse(new Admin());
        if (result == 0) {
            // 判断为信息正确
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("group", admin.getGroupName());
            request.getSession().setAttribute("role", Identities.ROLE_ADMIN);

            data.put("location", "/" + Identities.ROLE_ADMIN + "/home");
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
            this.logger.error("login: Unresolved result " + result);
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
            return new ModelAndView("/" + Identities.ROLE_ADMIN +
                    "/" + Identities.GROUP_RECRUIT_AFFAIRS + "/recruit-maintain");
        } else if (result == Status.ERR_EXAM_EXISTS) {
            model.addAttribute("msg", "已经有这项考试了。");
            return new ModelAndView("/" + Identities.ROLE_ADMIN +
                    "/" + Identities.GROUP_RECRUIT_AFFAIRS + "/recruit-maintain");
        } else if (result == Status.OK) {
            model.addAttribute("msg", "添加完成。");
            return new ModelAndView("/" + Identities.ROLE_ADMIN +
                    "/" + Identities.GROUP_RECRUIT_AFFAIRS + "/recruit-maintain");
        } else {
            this.logger.error("publishEnrollmentInfo: Unresolved result " + result);
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
    @ResponseBody
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
    @ResponseBody
    @NoNullArgs
    public R dropUser(Long id) {
        adminService.dropUser(id);
        return R.builder()
                .status(Status.OK)
                .message("ID为" + id + "的用户已经移除。")
                .build();
    }
}
