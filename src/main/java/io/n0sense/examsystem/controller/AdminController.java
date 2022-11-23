package io.n0sense.examsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public ModelAndView page(Model model){
        model.addAttribute("text", "Hello world");
        return new ModelAndView("admin");
    }
}
