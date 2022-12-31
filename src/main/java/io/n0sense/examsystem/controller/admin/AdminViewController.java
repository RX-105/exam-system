package io.n0sense.examsystem.controller.admin;

import com.sun.management.OperatingSystemMXBean;
import io.n0sense.examsystem.commons.SystemStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @Autowired
    private SystemStatistics statistics;

    @GetMapping("/status")
    public ModelAndView getStatusView(Model model){
        model.addAttribute("heap_usage",
                String.format("%.2f", statistics.getStat("heap_usage")));
        model.addAttribute("memory_usage",
                String.format("%.2f", statistics.getStat("system_memory_usage")));
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        model.addAttribute("cpu_usage",
                String.format("%.2f", osBean.getCpuLoad()));
        model.addAttribute("disk_usage",
                String.format("%.2f", statistics.getStat("disk_usage")));
        return new ModelAndView("/admin/status");
    }
}
