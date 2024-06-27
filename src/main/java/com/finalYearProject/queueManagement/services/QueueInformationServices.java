package com.finalYearProject.queueManagement.services;

import com.finalYearProject.queueManagement.model.BankManagerLogin;
import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.model.QueueInformation;
import com.finalYearProject.queueManagement.repository.BankManagerRepository;
import com.finalYearProject.queueManagement.repository.BranchRepo;
import com.finalYearProject.queueManagement.repository.BranchServiceOfferedRepo;
import com.finalYearProject.queueManagement.repository.QueueInformationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QueueInformationServices {

    @Autowired
    private QueueInformationRepo queueInformationRepo;

    @Autowired
    private BranchServiceOfferedRepo branchServiceOfferedRepo;

    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private  BankManagerRepository bankManagerLoginRepo;

    // Define the interval for the scheduler (e.g., every minute)
    private static final long QUEUE_UPDATE_INTERVAL = 60 * 1000; // in milliseconds


    // Method to save or update a queue information record
    public QueueInformation saveQueueInformation(QueueInformation queueInformation) {
        return queueInformationRepo.save(queueInformation);
    }

    // Method to retrieve all queue information records
    public List<QueueInformation> getAllQueueInformation() {
        return queueInformationRepo.findAll();
    }

    // Method to retrieve queue information by ID
    public Optional<QueueInformation> getQueueInformationById(Long id) {
        return queueInformationRepo.findById(id);
    }

    // Method to delete queue information by ID
    public void deleteQueueInformationById(Long id) {
        queueInformationRepo.deleteById(id);
    }

    // Additional methods as needed for specific requirements

    // Method to find queue information by branch ID
    public QueueInformation findByBranchId(Long branchId) {
        return queueInformationRepo.findByBranchId(branchId);
    }



    // Get the service by ID
    public void bookQueue(String email, Long serviceId, Long branchId) {
        // Get the service by ID
        Optional<BranchServiceOffered> optionalService = branchServiceOfferedRepo.findById(serviceId);
        if (optionalService.isPresent()) {
            BranchServiceOffered service = optionalService.get();

            // Get the time per user based on the service name
            int timePerUser = getTimePerUser(service.getName());
            System.out.println("++++++ TIME PER USER SERVICE IS"+timePerUser);

            // Update the queue information for the service
            updateQueueInfoForService(service, timePerUser, branchId);

            // You can handle other operations such as sending an email to the user here
        }
    }


    private int getTimePerUser(String serviceName) {
        // Implement the logic to determine the time per user based on the service name
        switch (serviceName) {
            case "Money Exchange":
                return 6;
            case "Technical Support":
                return 7;
            case "Mortgage":
                return 30;
            case "Insurance services":
                return 20;
            case "Current Saving":
                return 5;
            case "Loans":
                return 30;
            case "Card Center":
                return 10;
            default:
                return 100; // Default value if service name not found
        }
    }

    private void updateQueueInfoForService(BranchServiceOffered service, int timePerUser, Long branchId) {
        // Find the corresponding QueueInformation for the branch ID and service ID
        Optional<QueueInformation> optionalQueueInfo = queueInformationRepo.findByBranchIdAndServiceId(branchId, service.getId());

        if (optionalQueueInfo.isPresent()) {
            QueueInformation queueInfo = optionalQueueInfo.get();

            // Assuming a maximum queue size of 100
            int queueSize = Math.min(queueInfo.getQueueSize() + 1, 100); // Increment queue size by 1 for each booking
            int estimatedWaitingTime = queueInfo.getEstimatedWaitingHours() + timePerUser;

            // Update the queue size and estimated waiting time for the service
            queueInfo.setQueueSize(queueSize);
            queueInfo.setEstimatedWaitingHours(estimatedWaitingTime);
            queueInfo.setEstimatedTime(new Date());

            // Save the updated QueueInformation
            queueInformationRepo.save(queueInfo);
        } else {
            // If no QueueInformation object exists for the given branch and service, create a new one
            QueueInformation newQueueInfo = new QueueInformation();
            Optional<Branch> optionalBranch = branchRepo.findById(branchId);
            if (optionalBranch.isPresent()) {
                newQueueInfo.setBranch(optionalBranch.get());
                newQueueInfo.setService(service);
                newQueueInfo.setQueueSize(1); // Set initial queue size to 1
                newQueueInfo.setEstimatedWaitingHours(timePerUser); // Set initial waiting time based on time per user
                newQueueInfo.setEstimatedTime(new Date()); // Set initial estimated time to current time
                queueInformationRepo.save(newQueueInfo);
            } else {
                // Handle the case where the branch with the provided ID is not found
                // You may want to log an error or throw an exception
                return; // Exit the method if branch is not found
            }
        }
    }


    @Scheduled(fixedRate = QUEUE_UPDATE_INTERVAL)
    public void updateQueueInformation() {
        List<QueueInformation> queueInfos = queueInformationRepo.findAll();

        for (QueueInformation queueInfo : queueInfos) {
            int elapsedTime = calculateElapsedTime(queueInfo.getEstimatedTime());
            int timePerUser = getTimePerUser(queueInfo.getService().getName());

            if (queueInfo.getEstimatedWaitingHours()==0) {
                queueInfo.setQueueSize(0);

            }else if (queueInfo.getEstimatedWaitingHours()<timePerUser && queueInfo.getQueueSize()>0) {
            queueInfo.setQueueSize(1);
            }

            // Update queue information
            int updatedQueueSize = queueInfo.getEstimatedWaitingHours() / timePerUser;
            int updatedEstimatedWaitingHours = Math.max(queueInfo.getEstimatedWaitingHours() - elapsedTime, 0);


            queueInfo.setQueueSize(updatedQueueSize);
            queueInfo.setEstimatedWaitingHours(updatedEstimatedWaitingHours);

            // Save the updated QueueInformation
            queueInformationRepo.save(queueInfo);
        }
    }



    // Method to retrieve queue information for the authenticated manager's branch
    public QueueInformation getQueueInformationForManager(Long managerId) {
        // Get the branch ID associated with the manager
        Long branchId = getBranchIdForManager(managerId);

        // Fetch the queue information for the manager's branch
        return queueInformationRepo.findTopByBranchId(branchId);
    }

    // Method to get the branch ID for a given manager ID
    private Long getBranchIdForManager(Long managerId) {
        // Retrieve the BankManagerLogin entity using the managerId
        BankManagerLogin manager = bankManagerLoginRepo.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        // Return the branch ID associated with the manager
        return manager.getBranch().getId();
    }




    private int calculateElapsedTime(Date startTime) {
        if (startTime == null) {
            return 0; // Return 0 if start time is null
        }
        long currentTime = System.currentTimeMillis();
        long startTimeMillis = startTime.getTime();
        return (int) ((currentTime - startTimeMillis) / 60000); // Convert milliseconds to minutes
    }






}
