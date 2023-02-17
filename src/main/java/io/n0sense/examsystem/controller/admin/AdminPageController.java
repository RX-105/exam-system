package io.n0sense.examsystem.controller.admin;

import io.n0sense.examsystem.service.impl.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 把不需要功能只需要页面的URL映射起来。
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {
    private final SchoolService schoolService;
    private final Map<String, String> groupMap;
    @Value("${application.version}")
    private String version;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("version", version);
        model.addAttribute("identities", groupMap);
        model.addAttribute("schools", schoolService.findAll());
    }
    @GetMapping("/home")
    public String getHomeView(){
        return "/admin/home";
    }
}
