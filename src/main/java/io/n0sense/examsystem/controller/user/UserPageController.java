package io.n0sense.examsystem.controller.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserPageController {
    @Value("${application.version}")
    private String version;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("version", version);
    }

    @GetMapping("/student/home")
    public String getStudentHomeView() {
        return "/student/home";
    }
}
