package com.finalYearProject.queueManagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalYearProject.queueManagement.model.AdminLogin;
import com.finalYearProject.queueManagement.model.BookedUserInfo;
import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.repository.BankManagerRepository;
import com.finalYearProject.queueManagement.services.*;
import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private BranchServiceOfferedService branchServiceOfferedService;

    @Autowired
    private BankManagerRepository bankManagerLoginRepo;

    @Autowired
    private RatingsServices ratingsService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private BookedUserInfoService bookedUserInfoService;

    @Autowired
    private BranchServices branchService;

    @Autowired
    private AdminReport reportService;

    private static final Logger    logger = LoggerFactory.getLogger(ManagerController.class);

    @GetMapping("/admin_signup")
    public String showSignupForm(Model model) {
        return "admin_signup";
    }

    @PostMapping("/admin_signup")
    public String registerAdmin(@RequestParam String username, Model model) {
        AdminLogin admin = adminService.createAdmin(username);
        model.addAttribute("admin", admin);
        model.addAttribute("message", "Admin account created successfully. The password has been sent to the default email address.");
        return "admin_signup_success";
    }




    @GetMapping("/admin/admin_perform_login")
    public String AdminLogin() {
        return "redirect:/admin/system_admin/home"; // Redirect to admin home page after successful login
    }

    @GetMapping("/admin/admin_login")
    public String SystemAdminLoginPage() {
        return "admin_login";
    }



    @GetMapping("/admin/system_admin/home")
    public String SystemAdminHomePage(Model model) {
        // Get the authenticated Admin details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            // Add the admin username to the model
            model.addAttribute("username", username);
        }
        return "system_admin_home";
    }

    @GetMapping("/admin/statistics")
    public String showStatistics(Model model) {
        List<Object[]> mostVisitedBranches = adminService.getMostVisitedBranches();
        List<Object[]> mostSoughtServices = adminService.getMostSoughtServices();
        List<Object[]> peakHours = adminService.getPeakHours();

        model.addAttribute("mostVisitedBranches", mostVisitedBranches);
        model.addAttribute("mostSoughtServices", mostSoughtServices);
        model.addAttribute("peakHours", peakHours);

        model.addAttribute("dailyVisitors", adminService.getDailyVisitors());
        model.addAttribute("weeklyVisitors", adminService.getWeeklyVisitors());
        model.addAttribute("monthlyVisitors", adminService.getMonthlyVisitors());

        return "admin_view_statistics";
    }










    @GetMapping("/admin/pause/services")
    public String adminGetServices(Model model) {
        List<BranchServiceOffered> services = branchServiceOfferedService.getAllServices();
        model.addAttribute("services", services);
        return "admin_pause_services";
    }

    @PostMapping("/admin/services/{id}/enable")
    public String AdminEnableService(@PathVariable Long id) {
        logger.info("Enabling service with id {}", id);
        branchServiceOfferedService.AdminUpdateServiceStatus(id, true);
        return "redirect:/admin/pause/services";
    }

    @PostMapping("/admin/services/{id}/disable")
    public String adminDisableService(@PathVariable Long id) {
        logger.info("Disabling service with id {}", id);
        branchServiceOfferedService.AdminUpdateServiceStatus(id, false);
        return "redirect:/admin/pause/services";
    }


    @GetMapping("/admin/ratings-feedbacks")
    public String adminReportPage() {
        return "/admin_ratings_feedbacks";
    }




    @GetMapping("/admin/report")
    public String showReportPage(Model model) {
        // Show the report generation page
        return "admin_report";
    }

    @GetMapping("/admin/report/generate")
    public void generateReport(@RequestParam String timeframe, HttpServletResponse response) throws IOException, DocumentException {
        List<BookedUserInfo> reportData = reportService.getReportData(timeframe);
        reportService.generatePdfReport(reportData, timeframe, response);
    }







}

