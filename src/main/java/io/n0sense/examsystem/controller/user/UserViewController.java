package io.n0sense.examsystem.controller.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class UserViewController {
    @Value("${application.version}")
    private String version;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("version", version);
    }

    @GetMapping("/login")
    public ModelAndView getStudentLoginView(HttpSession session) {
        boolean remember;
        try {
            remember = Boolean.parseBoolean((String) session.getAttribute("remember"));
        } catch (ClassCastException e) {
            remember = (Boolean) session.getAttribute("remember");
        }
        if (remember) {
            return new ModelAndView("/student/home");
        } else {
            return new ModelAndView("/student/login");
        }
    }
}
