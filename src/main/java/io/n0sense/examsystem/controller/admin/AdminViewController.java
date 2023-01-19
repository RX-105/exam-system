package io.n0sense.examsystem.controller.admin;

import com.sun.management.OperatingSystemMXBean;
import io.n0sense.examsystem.commons.SystemStatistics;
import io.n0sense.examsystem.entity.Log;
import io.n0sense.examsystem.entity.Visits;
import io.n0sense.examsystem.repository.LogRepository;
import io.n0sense.examsystem.repository.VisitsRepository;
import io.n0sense.examsystem.service.impl.AdminService;
import io.n0sense.examsystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 实现管理员页面功能的控制器。
 */
@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @Autowired
    private SystemStatistics statistics;
    @Autowired
    private VisitsRepository visitsRepository;
    @Autowired
    private HibernateUtil hibernateUtil;
    @Autowired
    private AdminService adminService;

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
        Optional<Visits> optionalTodayVisit = visitsRepository.findById(LocalDate.now());
        Visits todayVisit = optionalTodayVisit.orElse(new Visits(LocalDate.now(), 1L));
        model.addAttribute("today_visit", todayVisit.getCount());
        Session session = hibernateUtil.getSessionJavaConfigFactory().openSession();
        Transaction transaction = session.beginTransaction();
        BigDecimal totalVisit = (BigDecimal) session
                .createSQLQuery("select sum(count) from visits")
                .getSingleResult();
        BigInteger totalUser = (BigInteger) session
                .createSQLQuery("select count(*) from visits")
                .getSingleResult();
        transaction.commit();
        model.addAttribute("total_visit", totalVisit.longValue());
        model.addAttribute("total_user", totalUser.longValue());

        return new ModelAndView("/admin/status");
    }

    @GetMapping("/login-history")
    public ModelAndView getLoginHistoryView(Integer page, Model model, HttpServletRequest request){
        Page<Log> loginHistoryPage = adminService.getUserLogins(
                (String) request.getSession().getAttribute("username"),
                (null == page ? 0 : page),
                10
        );
        List<Log> loginHistory = loginHistoryPage.toList();
        model.addAttribute("loginsPage", loginHistoryPage);
        model.addAttribute("logins", loginHistory);

        return new ModelAndView("/admin/login-history");
    }
}
