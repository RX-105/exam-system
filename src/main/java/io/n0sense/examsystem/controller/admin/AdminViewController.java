/*
 * This file is part of exam-system.
 *
 * exam-system is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * exam-system is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with exam-system. If not, see <https://www.gnu.org/licenses/>.
 */

package io.n0sense.examsystem.controller.admin;

import com.sun.management.OperatingSystemMXBean;
import io.n0sense.examsystem.commons.SystemStatistics;
import io.n0sense.examsystem.commons.constants.Stages;
import io.n0sense.examsystem.dto.ExamUserDTO;
import io.n0sense.examsystem.entity.*;
import io.n0sense.examsystem.service.impl.*;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 实现管理员页面功能的控制器。
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {
    private final LogService logService;
    private final VisitsService visitsService;
    private final AdminService adminService;
    private final BackupService backupService;
    private final StageService stageService;
    private final SchoolService schoolService;
    private final MajorService majorService;
    private final ExamService examService;
    private final Map<String, String> groupMap;
    private final HttpSession session;
    private final ThreadLocal<Admin> localAdmin = ThreadLocal.withInitial(() -> null);
    @Value("${application.version}")
    private String version;
    @Value("${application.admin-token}")
    private String adminToken;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("version", version);
        model.addAttribute("identities", groupMap);
        model.addAttribute("schools", schoolService.findAll());
    }

    /**
     * 检从session中获取用户信息，并保存到线程局部对象中，返回一个布尔值，用于表示检查结果。<br>
     * 如果无法获取用户信息，将会在model对象中写入错误信息，属性名为msg，且不设置localAdmin。
     * @param model Spring MVC的Model对象
     * @return 如果返回值是true，该方法将会设置线程局部对象localUser，否则在model中写入错误信息。
     * @apiNote 使用该方法并使用localAdmin对象后必须使用remove方法清除对象，否则将导致内存泄漏！
     */
    public boolean loadUserInfo(Model model) {
        Long uid = (Long) session.getAttribute("uid");
        if (uid == null) {
            model.addAttribute("msg", "请尝试重新登陆。");
            return false;
        }
        Optional<Admin> admin = adminService.findById(uid);
        if (admin.isEmpty()) {
            model.addAttribute("msg", "请尝试重新登陆。");
            return false;
        } else {
            localAdmin.set(admin.get());
            return true;
        }
    }

    @GetMapping("/login")
    public ModelAndView getLoginView(HttpSession session){
        boolean remember;
        try {
            remember = Boolean.parseBoolean((String) session.getAttribute("remember"));
        } catch (ClassCastException e) {
            remember = (Boolean) session.getAttribute("remember");
        }
        if (remember) {
            return new ModelAndView("/admin/home");
        } else {
            return new ModelAndView("/admin/login");
        }
    }

    @GetMapping("/register")
    public ModelAndView getRegisterView(@Nullable String token, Model model) {
        if (token != null && token.equals(adminToken)) {
            model.addAttribute("allowAdmin", true);
        } else {
            model.addAttribute("allowAdmin", false);
        }
        return new ModelAndView("/admin/register");
    }

    @GetMapping("/status")
    public ModelAndView getStatusView(Model model) {
        // ## 系统状态 ##
        model.addAttribute("heap_usage",
                String.format("%.2f", SystemStatistics.getHeapUsage()));
        model.addAttribute("memory_usage",
                String.format("%.2f", SystemStatistics.getSystemMemoryUsage()));
        // 总之就是我也不知道为什么，SystemStatistics类里面获取到的CPU占用不是1.0就是0.0，只能这么做了
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        model.addAttribute("cpu_usage",
                String.format("%.2f", 100 * osBean.getCpuLoad()));
        model.addAttribute("disk_usage",
                String.format("%.2f", SystemStatistics.getRootDiskUsage()));

        // ## 统计数据 ##
        model.addAttribute("today_visit", visitsService.getTodayVisits());
        model.addAttribute("total_visit", visitsService.getTotalVisits());
        model.addAttribute("total_user", visitsService.getTotalUsers());

        return new ModelAndView("/admin/status");
    }

    @GetMapping("/login-history")
    public ModelAndView getLoginHistoryView(Integer page, Model model, HttpServletRequest request){
        Page<Log> loginHistoryPage = logService.getUserLogins(
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

        return new ModelAndView("/admin/login-history");
    }

    @GetMapping("/sudoers/maintain")
    public ModelAndView getSudoersMaintainView(Integer page, Model model){
        Page<Admin> commonAdminPage = adminService.getAllAdmins(
                "sudoers",
                (null == page ? 0 : page),
                10
        );

        if (page != null && commonAdminPage.getTotalPages() < page){
            model.addAttribute("msg", "请勿玩弄页面参数哦。");
        } else {
            model.addAttribute("adminsPage", commonAdminPage);
            model.addAttribute("admins", commonAdminPage.toList());
        }

        return new ModelAndView("/admin/sudoers/maintain");
    }

    @GetMapping("/sudoers/db")
    public ModelAndView getSudoersDBView(Integer page, Model model) {
        Page<Backup> backupPage = backupService.findAll(
                (null == page ? 0 : page),
                10
        );
        List<Backup> backupList = backupPage.toList();
        if (page != null && backupPage.getTotalPages() < page){
            model.addAttribute("msg", "请勿玩弄页面参数哦。");
        } else {
            model.addAttribute("backups", backupList);
            model.addAttribute("backupPage", backupPage);
        }

        return new ModelAndView("/admin/sudoers/db");
    }

    @GetMapping("/recruit/stage-def")
    public ModelAndView getRecruitStageDefView(Integer page, Model model) {
        Page<Stage> stagePage =  stageService.findAll(
                (null == page ? 0 : page),
                10
        );
        List<Stage> stages = stagePage.toList();
        if (page != null && stagePage.getTotalPages() < page){
            model.addAttribute("msg", "请勿玩弄页面参数哦。");
        } else {
            model.addAttribute("stages", stages);
            model.addAttribute("stagePage", stagePage);
            model.addAttribute("stageMap", Stages.stages);
        }

        return new ModelAndView("/admin/recruit/stage-def");
    }

    @GetMapping("/recruit/recruit-maintain")
    public ModelAndView getRecruitMaintainView(Integer mpage, Integer epage, Long majorId, Model model) {
        Page<Major> majorPage =  majorService.findAll((null == mpage ? 0 : mpage), 10);
        List<Major> majors = majorPage.toList();
        if (mpage != null && majorPage.getTotalPages() < mpage){
            model.addAttribute("msg", "请勿玩弄页面参数哦。");
        } else {
            model.addAttribute("majors", majors);
            model.addAttribute("majorPage", majorPage);
        }

        if (majorId != null) {
            Page<Exam> examPage = examService.findAllByMajorId(majorId, (null == epage ? 0 : epage), Integer.MAX_VALUE);
            List<Exam> exams = examPage.toList();
            if (epage != null && examPage.getTotalPages() < epage) {
                model.addAttribute("msg", "请勿玩弄页面参数哦。");
            } else {
                model.addAttribute("exams", exams);
            }
        }

        return new ModelAndView("/admin/recruit/recruit-maintain");
    }

    @GetMapping("/recruit/confirm")
    public ModelAndView getRecruitConfirmView() {
        return new ModelAndView("/admin/recruit/confirm");
    }

    @GetMapping("/aca-aff/ticket-assign")
    public ModelAndView getAcaAffTicketAssignView(Model model) {
        loadUserInfo(model);
        return new ModelAndView("/admin/aca-aff/ticket-assign");
    }

    @GetMapping("/aca-aff/room-assign")
    public ModelAndView getAcaAffRoomAssignView(Integer page, Model model) {
        if (!loadUserInfo(model)) {
            return new ModelAndView("/admin/aca-aff/room-assign");
        }
        Page<ExamUserDTO> examUsers =  adminService.getExamUserInfo(
                localAdmin.get().getSchoolId(), (null == page ? 0 : page), 10
        );
        List<ExamUserDTO> examUserList = examUsers.toList();
        if (page != null && examUsers.getTotalPages() < page){
            model.addAttribute("msg", "请勿玩弄页面参数哦。");
        } else {
            model.addAttribute("students", examUserList);
            model.addAttribute("studentPage", examUsers);
        }
        return new ModelAndView("/admin/aca-aff/room-assign");
    }
}
