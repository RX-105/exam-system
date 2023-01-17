package io.n0sense.examsystem.controller.admin;

import io.n0sense.examsystem.commons.CommonStatus;
import io.n0sense.examsystem.commons.GroupConstants;
import io.n0sense.examsystem.entity.Admin;
import io.n0sense.examsystem.service.impl.AdminService;
import io.n0sense.examsystem.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 管理员页面中的REST API控制器。
 */
@Controller
@RequestMapping("/api/admin")
public class AdminRestController {
    @Autowired
    private AdminService adminService;
    private final Logger logger = LoggerFactory.getLogger(AdminRestController.class);

    @PostMapping({"/register"})
    public ModelAndView register(String username, String password, String groupName, HttpServletRequest request, Model model) {
        int result = this.adminService.register(username, password, groupName, IpUtil.getIpAddress(request));
        if (result == CommonStatus.OK) {
            // 检查判断为登陆成功
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("group", groupName);
            request.getSession().setAttribute("role", GroupConstants.ROLE_ADMIN);
            return new ModelAndView("/" + GroupConstants.ROLE_ADMIN + "/home");
        } else if (result == CommonStatus.ERR_USERNAME_IN_USE) {
            // 用户密码已经存在
            model.addAttribute("msg", "用户名已占用。");
            return new ModelAndView("/" + GroupConstants.ROLE_ADMIN + "/register");
        } else {
            // 其他错误
            this.logger.error("register: Unresolved result " + result);
            return new ModelAndView("404");
        }
    }

    @PostMapping({"/login"})
    public ModelAndView login(String username, String password, Model model, HttpServletRequest request) {
        int result = this.adminService.login(username, password);
        Admin admin = (Admin) this.adminService.findByName(username).get();
        if (result == 0) {
            // 判断为信息正确
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("group", admin.getGroupName());
            request.getSession().setAttribute("role", GroupConstants.ROLE_ADMIN);
            return new ModelAndView("/" + GroupConstants.ROLE_ADMIN + "/home");
        } else if (result == CommonStatus.ERR_INCORRECT_PASSWORD) {
            // 判断为密码错误
            model.addAttribute("msg", "密码错误。");
            return new ModelAndView("/" + GroupConstants.ROLE_ADMIN + "/login");
        } else if (result == CommonStatus.ERR_USER_NOT_FOUND) {
            // 判断为用户不存在
            model.addAttribute("msg", "没有这个用户。");
            return new ModelAndView("/" + GroupConstants.ROLE_ADMIN + "/login");
        } else {
            // 其他错误
            this.logger.error("login: Unresolved result " + result);
            return new ModelAndView("404");
        }
    }

    @PostMapping({"/publish"})
    public ModelAndView publishEnrollmentInfo(String majorName, int applicant, int enrollment, int admission, double score, String examName, LocalDateTime start, LocalDateTime end, Model model) {
        int result = this.adminService.addEnrollmentInfo(majorName, applicant, enrollment, admission, score, examName, start, end);
        if (result == CommonStatus.ERR_MAJOR_EXISTS) {
            model.addAttribute("msg", "已经有这个专业了。");
            return new ModelAndView("/" + GroupConstants.ROLE_ADMIN +
                    "/" + GroupConstants.GROUP_RECRUIT_AFFAIRS + "/recruit-maintain");
        } else if (result == CommonStatus.ERR_EXAM_EXISTS) {
            model.addAttribute("msg", "已经有这项考试了。");
            return new ModelAndView("/" + GroupConstants.ROLE_ADMIN +
                    "/" + GroupConstants.GROUP_RECRUIT_AFFAIRS + "/recruit-maintain");
        } else if (result == CommonStatus.OK) {
            model.addAttribute("msg", "添加完成。");
            return new ModelAndView("/" + GroupConstants.ROLE_ADMIN +
                    "/" + GroupConstants.GROUP_RECRUIT_AFFAIRS + "/recruit-maintain");
        } else {
            this.logger.error("publishEnrollmentInfo: Unresolved result " + result);
            return new ModelAndView("404");
        }
    }
}
