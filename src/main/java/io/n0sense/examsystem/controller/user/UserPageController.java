package io.n0sense.examsystem.controller.user;

import io.n0sense.examsystem.service.impl.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class UserPageController {
    @Value("${application.version}")
    private String version;
    private final SchoolService schoolService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("version", version);
        model.addAttribute("schools", schoolService.findAll());
    }

    @GetMapping("/student/home")
    public String getStudentHomeView() {
        return "/student/home";
    }

    @GetMapping("/student/register")
    public String getStudentRegisterView() {
        return "/student/register";
    }
}
