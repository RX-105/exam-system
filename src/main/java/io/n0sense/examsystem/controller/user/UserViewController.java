package io.n0sense.examsystem.controller.user;

import io.n0sense.examsystem.commons.constants.Stages;
import io.n0sense.examsystem.entity.Log;
import io.n0sense.examsystem.service.impl.LogService;
import io.n0sense.examsystem.service.impl.SchoolService;
import io.n0sense.examsystem.service.impl.StageService;
import io.n0sense.examsystem.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class UserViewController {
    @Value("${application.version}")
    private String version;
    private final HttpServletRequest request;
    private final HttpSession session;
    private final LogService logService;
    private final SchoolService schoolService;
    private final StageService stageService;
    private final UserService userService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("version", version);
        model.addAttribute("schools", schoolService.findAll());
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

    @GetMapping("/login-history")
    public ModelAndView getStudentLoginHistoryView(Integer page, Model model) {
        var loginHistoryPage = logService.getUserLogins(
                (String) request.getSession().getAttribute("username"),
                (null == page ? 0 : page),
                10
        );
        List<Log> loginHistory = loginHistoryPage.toList();

        if (page != null && loginHistoryPage.getTotalPages() < page){
            model.addAttribute("msg", "请勿玩弄页面参数哦。");
        } else {
            model.addAttribute("loginsPage", loginHistoryPage);
            model.addAttribute("logins", loginHistory);
        }

        return new ModelAndView("/student/login-history");
    }

    @GetMapping("/registration-notice")
    public ModelAndView getStudentRegistrationNoticeView(Long schoolId, Model model) {
        if (schoolId != null) {
            String schoolName = schoolService.findSchool(schoolId).getName();
            model.addAttribute("stages", stageService.findAllStageBySchoolId(schoolId));
            model.addAttribute("schoolName", schoolName);
            model.addAttribute("stageMap", Stages.stages);
        }
        return new ModelAndView("/student/registration-notice");
    }

    @GetMapping("/exam-register")
    public ModelAndView getStudentExamRegisterView(Model model) {
        model.addAttribute("user",
                userService.findById((Long) session.getAttribute("uid")).orElseThrow());
        return new ModelAndView("/student/exam-register");
    }

    @GetMapping("/upload-avatar")
    public ModelAndView getStudentUploadAvatarView() {
        return new ModelAndView("/student/upload-avatar");
    }
}
