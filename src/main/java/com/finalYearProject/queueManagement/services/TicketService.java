package com.finalYearProject.queueManagement.services;

import com.finalYearProject.queueManagement.model.*;
import com.finalYearProject.queueManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepo ticketSystemRepository;

    @Autowired
    private BranchServiceOfferedRepo branchServiceOfferedRepository;

    @Autowired
    private BranchRepo branchRepository;

    @Autowired
    private QueueInformationRepo queueInformationRepo;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private BranchServices bankManagerService;

    @Autowired
    private BankManagerRepository bankManagerLoginRepo;




    public void sendTicket(String email, Long serviceId, Long branchId) {


        // Retrieve branch and service information
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch not found"));
        BranchServiceOffered service = branchServiceOfferedRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Service not found"));

        Optional<QueueInformation> queueInformation = queueInformationRepo.findByBranchIdAndServiceId(branchId, service.getId());


        // Generate ticket number
        String ticketNumber = generateTicketNumber(service);

        // Calculate estimated serving time
        LocalDateTime estimatedServingTime = LocalDateTime.now().plusMinutes(queueInformation.get().getEstimatedWaitingHours());

        // Save ticket information
        TicketSystem ticket = new TicketSystem();
        ticket.setTicketNumber(ticketNumber);
        ticket.setBranch(branch);
        ticket.setService(service);
        ticketSystemRepository.save(ticket);

        // Send email to the user
        sendConfirmationEmail(email, ticketNumber, service.getName(), branch.getBkBranchName(), branch.getAddress(), estimatedServingTime);
    }

    private String generateTicketNumber(BranchServiceOffered service) {
        String serviceName = service.getName().replaceAll("\\s+", "");
        List<TicketSystem> tickets = ticketSystemRepository.findByServiceOrderByTicketNumber(service);
        int ticketCount = tickets.size() + 1;
        return serviceName.toUpperCase() + "-" + ticketCount;
    }

    private void sendConfirmationEmail(String email, String ticketNumber, String serviceName, String branchName, String branchLocation, LocalDateTime estimatedServingTime) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Queue Booking Confirmation");
        message.setText("Hello,\n\n"
                + "You have successfully booked a queue.\n"
                + "Ticket Number: " + ticketNumber + "\n"
                + "Service Name: " + serviceName + "\n"
                + "Branch Name: " + branchName + "\n"
                + "Branch Location: " + branchLocation + "\n"
                + "Estimated Serving Time: " + formatDateTime(estimatedServingTime) + "\n\n"
                + "Thank you for using our service.");

        emailSender.send(message);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}




