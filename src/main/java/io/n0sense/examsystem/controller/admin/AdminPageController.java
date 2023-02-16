package io.n0sense.examsystem.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 把不需要功能只需要页面的URL映射起来。
 */
@Controller
@RequestMapping("/admin")
public class AdminPageController {
    @GetMapping("/home")
    public String getHomeView(){
        return "/admin/home";
    }

    @GetMapping("/register")
    public String getRegisterView() {
        return "/admin/register";
    }
}
