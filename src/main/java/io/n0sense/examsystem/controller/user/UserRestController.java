package io.n0sense.examsystem.controller.user;

import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.commons.constants.Identities;
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

import jakarta.servlet.http.HttpServletRequest;

@Controller()
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class UserRestController {
    private final Logger logger = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;

    @PostMapping("/login")
    public ModelAndView login(String username, String password, Model model, HttpServletRequest request) {
        int result = userService.login(username, password);
        if (result == Status.OK) {
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("role", Identities.ROLE_STUDENT.getValue());
            return new ModelAndView("/" + Identities.ROLE_STUDENT.getValue() + "/home");
        } else if (result == Status.ERR_INCORRECT_PASSWORD) {
            model.addAttribute("msg", "密码错误。");
            return new ModelAndView("/" + Identities.ROLE_STUDENT.getValue() + "/home");
        } else if (result == Status.ERR_USER_NOT_FOUND) {
            model.addAttribute("msg", "没有这个用户。");
            return new ModelAndView("/" + Identities.ROLE_STUDENT.getValue() + "/home");
        } else {
            this.logger.error("login: Unresolved result " + result);
            return new ModelAndView("404");
        }
    }

    @PostMapping("/register")
    public ModelAndView register(String username, String password, Model model, HttpServletRequest request){
        int result = userService.register(username, password, IpUtil.getIpAddress(request));
        if (result == Status.OK) {
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("role", Identities.ROLE_STUDENT.getValue());
            return new ModelAndView("/" + Identities.ROLE_STUDENT.getValue() + "/home");
        } else if (result == Status.ERR_USERNAME_IN_USE) {
            model.addAttribute("msg", "用户名已占用。");
            return new ModelAndView("/" + Identities.ROLE_STUDENT.getValue() + "/home");
        } else {
            this.logger.error("register: Unresolved result " + result);
            return new ModelAndView("404");
        }
    }
}
