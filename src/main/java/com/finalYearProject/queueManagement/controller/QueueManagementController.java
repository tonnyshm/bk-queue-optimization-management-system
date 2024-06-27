package com.finalYearProject.queueManagement.controller;
import java.util.Optional;

import com.finalYearProject.queueManagement.model.BookedUserInfo;
import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.model.QueueInformation;
import com.finalYearProject.queueManagement.repository.BranchServiceOfferedRepo;
import com.finalYearProject.queueManagement.repository.QueueInformationRepo;
import com.finalYearProject.queueManagement.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class QueueManagementController {

    @Autowired
    private QueueInformationServices queueInformationServices;
    @Autowired
    private BranchServiceOfferedService branchServiceOfferedService;

    @Autowired
    private QueueInformationRepo queueInformationRepo;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private BookedUserInfoService bookedUserInfoService;
    @Autowired
    private BranchServiceOfferedRepo branchServiceOfferedRepo;

    @Autowired
    private BranchServices branchServices;


    @GetMapping("/queue-management/{branchId}")
    public String showQueueManagementDetails(@PathVariable Long branchId, Model model) {
        // Retrieve the list of bank services offered by the branch
        Set<BranchServiceOffered> offeredServices = branchServiceOfferedService.getServicesByBranchId(branchId);

        // Create a map to store QueueInformation objects keyed by service ID
        Map<Long, QueueInformation> queueInfoMap = new HashMap<>();

        // Populate the map with QueueInformation objects
        for (BranchServiceOffered service : offeredServices) {
            Optional<QueueInformation> optionalQueueInfo = queueInformationRepo.findByBranchIdAndServiceId(branchId, service.getId());
            optionalQueueInfo.ifPresent(queueInfo -> queueInfoMap.put(service.getId(), queueInfo));
            if (!optionalQueueInfo.isPresent()) {
                queueInfoMap.put(service.getId(), null);
            }

        }

        // Pass the offered services and queue information map to the template
        model.addAttribute("offeredServices", offeredServices);
        model.addAttribute("queueInfoMap", queueInfoMap);

        return "queue_management_page"; // Return the template for displaying queue management details
    }




//    @PostMapping("/book-queue")
//    public String bookQueue(
//            @RequestParam("email") String email,
//            @RequestParam("service") Long serviceId,
//            @RequestParam("branch") Long branchId, Model model) {
//
//        // Retrieve branch and service by their IDs
//        Branch branch = branchServices.getBranchById(branchId);
//        Optional<BranchServiceOffered> optionalService = branchServiceOfferedRepo.findById(serviceId);
//
//        if (optionalService.isPresent() && optionalService.get().isEnabled()) {
//            BranchServiceOffered branchServiceOffered = optionalService.get();
//
//            // Call the service method to update the queue
//            queueInformationServices.bookQueue(email, serviceId, branchId);
//            System.out.println("///////////SUCCESS UPDATED QUEUE//////////");
//
//            // Send the ticket to the user
//            ticketService.sendTicket(email, serviceId, branchId);
//            System.out.println("///////////SUCCESS TICKET SENT TO USER//////////");
//
//            // Save the user's booking information
//            bookedUserInfoService.saveUserBookedRecord(email, branch, branchServiceOffered);
//            System.out.println("///////////SUCCESS USER BOOKING INFO SAVED//////////");
//
//            // Redirect the user to a confirmation page
//            return "redirect:/booking_confirmation";
//        } else {
//            model.addAttribute("message", "The requested service is currently unavailable. Please check back later.");
//            return "redirect:/service-unavailable";
//        }
//
//    }

    @PostMapping("/book-queue")
    public String bookQueue(
            @RequestParam("email") String email,
            @RequestParam("service") Long serviceId,
            @RequestParam("branch") Long branchId, Model model) {

        // Retrieve branch and service by their IDs
        Branch branch = branchServices.getBranchById(branchId);
        Optional<BranchServiceOffered> optionalService = branchServiceOfferedRepo.findById(serviceId);

        if (optionalService.isPresent()) {
            BranchServiceOffered branchServiceOffered = optionalService.get();

            // Check if the service is enabled globally or at the branch level
            boolean isServiceEnabledGlobally = branchServiceOffered.isEnabled();
            boolean isServiceEnabledAtBranch = branchServices.isServiceEnabled(branchServices.getManagerUsernameByBranchId(branchId), serviceId);
            System.out.println("|||||||||||GLOBALLY IS: "+isServiceEnabledGlobally+" |||||||||||||||||||");
            System.out.println("|||||||||||||LOCALLY IS: "+isServiceEnabledAtBranch+" |||||||||||||||||||");

            if (isServiceEnabledGlobally && isServiceEnabledAtBranch) {
                // Call the service method to update the queue
                queueInformationServices.bookQueue(email, serviceId, branchId);

                // Send the ticket to the user
                ticketService.sendTicket(email, serviceId, branchId);

                // Save the user's booking information
                bookedUserInfoService.saveUserBookedRecord(email, branch, branchServiceOffered);

                // Redirect the user to a confirmation page
                return "redirect:/booking_confirmation";
            } else {
                model.addAttribute("message", "The requested service is currently unavailable. Please check back later.");
                return "redirect:/service-unavailable";
            }
        } else {
            model.addAttribute("message", "The requested service does not exist.");
            return "redirect:/service-unavailable";
        }
    }

    @GetMapping("/service-unavailable")
    public String serviceUnavailablePage(Model model) {
        model.addAttribute("service_unavailable", new QueueInformation());
        return "service_unavailable";
    }

}
