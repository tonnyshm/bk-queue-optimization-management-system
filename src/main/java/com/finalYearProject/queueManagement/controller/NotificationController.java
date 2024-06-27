package com.finalYearProject.queueManagement.controller;

import com.finalYearProject.queueManagement.model.BookedUserInfo;
import com.finalYearProject.queueManagement.model.EmailRequest;
import com.finalYearProject.queueManagement.repository.BookUserInfoRepo;
import com.finalYearProject.queueManagement.repository.BranchRepo;
import com.finalYearProject.queueManagement.repository.BranchServiceOfferedRepo;
import com.finalYearProject.queueManagement.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class NotificationController {

    @Autowired
    private BookUserInfoRepo bookedUserInfoRepository;

    @Autowired
    private BranchRepo branchRepository;

    @Autowired
    private BranchServiceOfferedRepo branchServiceRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/admin/sendEmailsForm")
    public String showEmailForm(Model model) {
        model.addAttribute("emailRequest", new EmailRequest());
        model.addAttribute("branches", branchRepository.findAll());
        model.addAttribute("services", branchServiceRepository.findAll());
        return "sendEmailsForm";
    }

    @PostMapping("/sendEmails")
    public String sendEmails(EmailRequest emailRequest, Model model) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime;

        switch (emailRequest.getTimeRange()) {
            case "today":
                startTime = now.withHour(0).withMinute(0).withSecond(0);
                break;
            case "lastHour":
                startTime = now.minusHours(1);
                break;
            case "last30Min":
                startTime = now.minusMinutes(30);
                break;
            case "lastMonth":
                startTime = now.minusMonths(1).withDayOfMonth(1);
                break;
            default:
                startTime = LocalDateTime.MIN;
        }

        List<BookedUserInfo> bookedUsers = bookedUserInfoRepository.findUsersByVisitTimeRangeAndFilters(
                startTime, now, emailRequest.getBranchId(), emailRequest.getServiceId());
        List<String> userEmails = bookedUsers.stream().map(BookedUserInfo::getUserEmail).collect(Collectors.toList());

        emailService.sendBulkEmail(userEmails, emailRequest.getSubject(), emailRequest.getMessage());

        model.addAttribute("message", "Emails sent successfully!");
        return "result_bulk_email"; // A view to display the result message
    }
}

