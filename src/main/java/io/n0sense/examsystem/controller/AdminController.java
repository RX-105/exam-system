package io.n0sense.examsystem.controller;

import io.n0sense.examsystem.commons.CommonStatus;
import io.n0sense.examsystem.entity.ResponseEntity;
import io.n0sense.examsystem.service.AdminService;
import io.n0sense.examsystem.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class AdminController {
    @Autowired
    private AdminService adminService;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @PostMapping("/register")
    public ModelAndView register(String username, String password, String groupName, HttpServletRequest request, Model model){
        int result = adminService.register(username, password, groupName, IpUtil.getIpAddress(request));
        if (result == CommonStatus.OK){
            request.getSession().setAttribute("username", username);
            return new ModelAndView("index");
        } else if (result == CommonStatus.ERR_USERNAME_IN_USE){
            model.addAttribute("msg", "用户名已占用。");
            return new ModelAndView("register");
        } else {
            logger.error("register: Unresolved result "+result);
            return new ModelAndView("404");

        }
    }
    @PostMapping("/login")
    public ModelAndView login(String username, String password, Model model){
        int result = adminService.login(username, password);
        if (result == CommonStatus.OK){
            return new ModelAndView("index");
        } else if (result == CommonStatus.ERR_INCORRECT_PASSWORD) {
            model.addAttribute("msg", "密码错误。");
            return new ModelAndView("login");
        } else if (result == CommonStatus.ERR_USER_NOT_FOUND){
            model.addAttribute("msg", "没有这个用户。");
            return new ModelAndView("login");
        } else {
            logger.error("login: Unresolved result "+result);
            return new ModelAndView("404");
        }
    }
}
