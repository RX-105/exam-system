package io.n0sense.examsystem.controller.admin;

import io.n0sense.examsystem.commons.SystemStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @Autowired
    private SystemStatistics statistics;

    @GetMapping("/status")
    public ModelAndView getStatusView(Model model){
        long free = (long) statistics.getStat("heap_free_memory");
        long total = (long) statistics.getStat("heap_total_memory");
        model.addAttribute("memory", String.format("%.2f", (double)free/total * 100));
        return new ModelAndView("/admin/status");
    }
}
