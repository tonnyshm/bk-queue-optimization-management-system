package com.finalYearProject.queueManagement.controller;

import com.finalYearProject.queueManagement.model.BankManagerLogin;
import com.finalYearProject.queueManagement.services.CustomUserDetails;
import com.finalYearProject.queueManagement.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
public class StatisticsController {


    @Autowired
    private StatisticsService statisticsService;


//    @GetMapping("/bank-manager/stats")
//    public String managerReportPage() {
//        return "/bank_manager_view_statistics";
//    }

    @GetMapping("/bank-manager/manager/statistics")
    public String viewStatistics(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            BankManagerLogin manager = (BankManagerLogin) userDetails.getUser();

            if (manager != null && manager.getBranch() != null) {
                Long branchId = manager.getBranch().getId();
                String branchName = manager.getBranch().getBkBranchName();
                String managerUsername = manager.getUsername();

                long dailyVisitors = statisticsService.getDailyVisitors(branchId);
                Map<String, Long> mostSoughtServices = statisticsService.getMostSoughtServices(branchId);
                long totalRatings = statisticsService.getTotalRatings(branchId);
                long totalFeedbacks = statisticsService.getTotalFeedbacks(branchId);
                List<Object[]> peakHours = statisticsService.getPeakHours(branchId);

                model.addAttribute("dailyVisitors", dailyVisitors);
                model.addAttribute("mostSoughtServices", mostSoughtServices);
                model.addAttribute("totalRatings", totalRatings);
                model.addAttribute("totalFeedbacks", totalFeedbacks);
                model.addAttribute("peakHours", peakHours);
                model.addAttribute("branchName", branchName);
                model.addAttribute("managerUsername", managerUsername);

                return "bank_manager_view_statistics";
            }
        }

        return "error";
    }
}
