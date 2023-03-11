package io.n0sense.examsystem.controller.user;

import io.n0sense.examsystem.commons.constants.Stages;
import io.n0sense.examsystem.config.properties.DevProperties;
import io.n0sense.examsystem.entity.Log;
import io.n0sense.examsystem.entity.Stage;
import io.n0sense.examsystem.entity.User;
import io.n0sense.examsystem.service.impl.*;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class UserViewController {
    private final ThreadLocal<User> localUser = ThreadLocal.withInitial(() -> null);
    @Value("${application.version}")
    private String version;
    private final HttpServletRequest request;
    private final HttpSession session;
    private final DevProperties devProperties;
    private final LogService logService;
    private final SchoolService schoolService;
    private final StageService stageService;
    private final UserService userService;
    private final MajorService majorService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("version", version);
        model.addAttribute("schools", schoolService.findAll());
    }

    /**
     * 检查当前时间是否在当前学校对于给定阶段定义的时间之内。返回内容是一个布尔值，为true时表示满足时间段要求，为
     * false表示不满足时间段要求，并在model对象中写入msg信息对象（字符串）。<br>
     * 此外，配置文件中的配置也会导致本方法的返回结果发生变化，比如application.debug.validate-stage-time
     * 置为false时，本方法将不会检查阶段时间，而是直接返回null。<br>
     * 如果返回值是true，该方法将会设置线程局部对象localUser。
     * @param model SpringMVC的{@link org.springframework.ui.Model Model}对象，可以通过Spring框架注入获取。
     * @param stage 指定检查时间的阶段，在{@link Stages}里有定义。
     * @return 一个布尔值，以true表示时间符合要求，反之亦然。
     */
    public boolean checkStageValidity(Model model, Map.Entry<String, String> stage) {
        Long uid = (Long) session.getAttribute("uid");
        if (uid == null) {
            model.addAttribute("msg", "请尝试重新登陆。");
            return false;
        }

        localUser.set(userService.findById(uid).orElseThrow());
        if (!devProperties.getValidateStageTime()) {
            return true;
        }
        if (localUser.get().getSchoolId() == null) {
            model.addAttribute("msg", "请先登记报考学校。");
            return false;
        }

        List<Stage> stagesList = stageService.findAllStageBySchoolIdAndName(
                localUser.get().getSchoolId(), stage.getKey());
        if (stagesList.size() == 0) {
            model.addAttribute("msg",
                    "学校没有设置["+stage.getValue()+"]阶段时间，请联系报考学校。");
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        boolean timeExceeded = true;
        for (Stage s : stagesList) {
            if (now.isAfter(s.getStartTime()) && now.isBefore(s.getEndTime())) {
                timeExceeded = false;
                break;
            }
        }
        if (timeExceeded) {
            model.addAttribute("msg", "当前时间段不在["+stage.getValue()+"]阶段时间段内。");
            return false;
        } else {
            return true;
        }
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
            String schoolName = schoolService.findSchool(schoolId).orElseThrow().getName();
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
    public ModelAndView getStudentUploadAvatarView(Model model) {
        checkStageValidity(model, Stages.REGISTER);
        return new ModelAndView("/student/upload-avatar");
    }

    @GetMapping("/register-form-print")
    public ModelAndView getStudentRegisterFormPrintVIew(Model model) {
        if (checkStageValidity(model, Stages.REGISTER)) {
            model.addAttribute("user", localUser.get());
            model.addAttribute("school",
                    schoolService.findSchool(localUser.get().getSchoolId()).orElseThrow());
            model.addAttribute("major",
                    majorService.findById(localUser.get().getMajor()).orElseThrow());
        }
        return new ModelAndView("/student/register-form-print");
    }
}
