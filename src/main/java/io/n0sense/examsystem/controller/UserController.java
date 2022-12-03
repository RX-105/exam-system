package io.n0sense.examsystem.controller;

import io.n0sense.examsystem.commons.CommonStatus;
import io.n0sense.examsystem.commons.GroupConstants;
import io.n0sense.examsystem.entity.User;
import io.n0sense.examsystem.service.impl.UserService;
import io.n0sense.examsystem.util.IpUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller()
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/login")
    public ModelAndView login(String username, String password, Model model, HttpServletRequest request) {
        int result = userService.login(username, password);
        if (result == CommonStatus.OK) {
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("role", GroupConstants.ROLE_STUDENT);
            return new ModelAndView("/" + GroupConstants.ROLE_STUDENT + "/home");
        } else if (result == CommonStatus.ERR_INCORRECT_PASSWORD) {
            model.addAttribute("msg", "密码错误。");
            return new ModelAndView("/" + GroupConstants.ROLE_STUDENT + "/home");
        } else if (result == CommonStatus.ERR_USER_NOT_FOUND) {
            model.addAttribute("msg", "没有这个用户。");
            return new ModelAndView("/" + GroupConstants.ROLE_STUDENT + "/home");
        } else {
            this.logger.error("login: Unresolved result " + result);
            return new ModelAndView("404");
        }
    }

    @PostMapping("/register")
    public ModelAndView register(String username, String password, Model model, HttpServletRequest request){
        int result = userService.register(username, password, IpUtil.getIpAddress(request));
        if (result == CommonStatus.OK) {
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("role", GroupConstants.ROLE_STUDENT);
            return new ModelAndView("/" + GroupConstants.ROLE_STUDENT + "/home");
        } else if (result == CommonStatus.ERR_USERNAME_IN_USE) {
            model.addAttribute("msg", "用户名已占用。");
            return new ModelAndView("/" + GroupConstants.ROLE_STUDENT + "/home");
        } else {
            this.logger.error("register: Unresolved result " + result);
            return new ModelAndView("404");
        }
    }
}
