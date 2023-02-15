package io.n0sense.examsystem.controller.admin;

import com.sun.management.OperatingSystemMXBean;
import io.n0sense.examsystem.commons.SystemStatistics;
import io.n0sense.examsystem.entity.Admin;
import io.n0sense.examsystem.entity.Backup;
import io.n0sense.examsystem.entity.Log;
import io.n0sense.examsystem.entity.Stage;
import io.n0sense.examsystem.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.management.ManagementFactory;
import java.util.List;

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

    @GetMapping("/sudoers/stage-def")
    public ModelAndView getSudoersStageDefView(Integer page, Model model) {
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
        }

        return new ModelAndView("/admin/sudoers/stage-def");
    }
}
