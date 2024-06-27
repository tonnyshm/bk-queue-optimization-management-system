package com.finalYearProject.queueManagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finalYearProject.queueManagement.model.*;
import com.finalYearProject.queueManagement.repository.BankManagerRepository;
import com.finalYearProject.queueManagement.services.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Controller
public class ManagerController {

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
    private BankManagerLoginService bankManagerLoginService;

    @Autowired
    private QueueInformationServices queueInformationServices;

    private static final Logger log = LoggerFactory.getLogger(ManagerController.class);








    @GetMapping("/bank-manager/services")
    public String getServices(Principal principal, Model model) {
        Set<BranchServiceOffered> services = bankManagerLoginService.getServicesWithBranchStatus(principal.getName());
        model.addAttribute("services", services);
        return "pause_service";
    }


    @PostMapping("/bank-manager/services/{id}/enable")
    public String enableService(@PathVariable Long id, Principal principal) {
        bankManagerLoginService.updateServiceStatus(principal.getName(), id, true);
        return "redirect:/bank-manager/services";
    }

    @PostMapping("/bank-manager/services/{id}/disable")
    public String disableService(@PathVariable Long id, Principal principal) {
        bankManagerLoginService.updateServiceStatus(principal.getName(), id, false);
        return "redirect:/bank-manager/services";
    }


//    @GetMapping("/bank-manager/pause-services")
//    public String showManageServicesPage(Model model, Principal principal) {
//        BankManagerLogin manager = bankManagerLoginRepo.findByUsername(principal.getName());
//        Set<BranchServiceOffered> services = branchServiceOfferedService.getServicesForManagerBranch(manager);
//        model.addAttribute("services", services);
//        return "pause_service";
//    }
//
//    @PostMapping("/bank-manager/manager/toggle-service")
//    public String managerToggleService(@RequestParam Long serviceId, @RequestParam boolean enabled, Principal principal) {
//        BankManagerLogin manager = bankManagerLoginRepo.findByUsername(principal.getName());
//        if (enabled) {
//            branchServiceOfferedService.enableService(serviceId, manager);
//        } else {
//            branchServiceOfferedService.disableService(serviceId, manager);
//        }
//        return "redirect:/bank-manager/pause-services";
//    }




    @GetMapping("/bank-manager/view-queue")
    public String getQueueInformationForManager(Authentication authentication, Model model) {
        // Retrieve the manager's ID from the authentication object
        Long managerId = getManagerIdFromAuthentication(authentication);

        // Fetch the queue information for the manager's branch
        QueueInformation queueInformation = queueInformationServices.getQueueInformationForManager(managerId);

        // Add queue information to the model
        model.addAttribute("queueInformation", queueInformation);

        return "bank_manager_queue_information"; // Thymeleaf template or any view resolver you use
    }

    private Long getManagerIdFromAuthentication(Authentication authentication) {
        // Retrieve the principal from the SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            BankManagerLogin manager = (BankManagerLogin) userDetails.getUser(); // Get the manager entity from userDetails

            if (manager != null && manager.getBranch() != null) {
                return manager.getBranch().getId(); // Return the branch ID associated with the manager
            }
        }
        return null; // Handle the case where the manager or branch is null
    }




    @GetMapping("/bank-manager/ratings-feedback")
    public String managerRatingsFeedbacksPage(Model model) {
        // Get the authenticated user's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            System.out.println("//////////++++ USERNAME IS:///|||||/////:"+username);
            // Add the username to the model
            model.addAttribute("username", username);
        }
        return "/manager_ratings_feedbacks";
    }

    @GetMapping("/bank-manager/manager/ratings")
    public String viewBranchRatings(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            BankManagerLogin manager = (BankManagerLogin) userDetails.getUser();

            if (manager != null && manager.getBranch() != null) {
                Long branchId = manager.getBranch().getId();
                List<Ratings> ratings = ratingsService.getRatingsByBranchId(branchId);
                model.addAttribute("ratings", ratings);
                model.addAttribute("username", manager.getUsername());
                return "manager_view_ratings";
            }
        }

        model.addAttribute("errorMessage", "User is not authenticated or no branch found.");
        return "error";
    }


    @GetMapping("/bank-manager/manager/feedbacks")
    public String viewBranchFeedbacks(Model model) {
        // Get the authenticated user details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            BankManagerLogin manager = (BankManagerLogin) userDetails.getUser();

            if (manager != null && manager.getBranch() != null) {
                Long branchId = manager.getBranch().getId();
                List<Feedbacks> feedbacks = feedbackService.getFeedbacksByBranchIdAndVisibility(branchId, false);
                model.addAttribute("feedbacks", feedbacks);
                model.addAttribute("username", manager.getUsername());
                return "manager_view_feedbacks";
            }
        }

        model.addAttribute("error", "User is not authenticated or no branch found.");
        return "error";
    }


    @GetMapping("/bank-manager/report")
    public String managerReportPage() {
        return "/bank_manager_generate_report";
    }


    @GetMapping("/bank-manager/generate-report")
    public void generateReport(
            @RequestParam("timeFrame") String timeFrame,
            HttpServletResponse response) throws DocumentException, IOException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails userDetails = (CustomUserDetails) principal;
        BankManagerLogin manager = (BankManagerLogin) userDetails.getUser();
        Long branchId = manager.getBranch().getId();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = null;

        switch (timeFrame.toUpperCase()) {
            case "DAILY":
                startDate = now.minusDays(1);
                break;
            case "WEEKLY":
                startDate = now.minusWeeks(1);
                break;
            case "MONTHLY":
                startDate = now.minusMonths(1);
                break;
            case "YEARLY":
                startDate = now.minusYears(1);
                break;
        }

        List<BookedUserInfo> bookedUsers = bookedUserInfoService.getBookedUsersByBranchAndDateRange(branchId, startDate, now);

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report.pdf");

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Branch Visit Report\n\n", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        font = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph info = new Paragraph("Branch: " + manager.getBranch().getBkBranchName() + "\nManager: " + manager.getUsername() + "\nGenerated Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) + "\n\n", font);
        document.add(info);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("User Email", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Visited Date", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Branch", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Service", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(cell);

        for (BookedUserInfo user : bookedUsers) {
            table.addCell(user.getUserEmail());
            table.addCell(user.getUserVisitedDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")));
            table.addCell(user.getBranch().getBkBranchName());
            table.addCell(user.getService().getName());
        }

        document.add(table);
        document.close();
    }

}

