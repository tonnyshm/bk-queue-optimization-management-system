package com.finalYearProject.queueManagement.services;

import com.finalYearProject.queueManagement.model.BankManagerLogin;
import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.repository.BankManagerRepository;
import com.finalYearProject.queueManagement.repository.BranchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchServices {

    @Autowired
    private BranchRepo branchRepo;


    @Autowired
    private BankManagerRepository bankManagerLoginRepo;

    public Branch getBranchByManager(String managerUsername) {
        return bankManagerLoginRepo.findByUsername(managerUsername)
                .getBranch();
    }

    public void updateServiceStatus(String managerUsername, Long serviceId, boolean enabled) {
        BankManagerLogin manager = bankManagerLoginRepo.findByUsername(managerUsername);

        BranchServiceOffered service = new BranchServiceOffered();
        service.setId(serviceId);

        manager.getServiceStatus().put(service, enabled);
        bankManagerLoginRepo.save(manager);
    }


    public boolean isServiceEnabled(String managerUsername, Long serviceId) {
        BankManagerLogin manager = bankManagerLoginRepo.findByUsername(managerUsername)
             ;
        BranchServiceOffered service = new BranchServiceOffered();
        service.setId(serviceId);
        return manager.getServiceStatus().getOrDefault(service, true); // Default to true if not found
    }

    public String getManagerUsernameByBranchId(Long branchId) {
        return bankManagerLoginRepo.findByBranchId(branchId)
                .orElseThrow(() -> new RuntimeException("Manager not found for the branch"))
                .getUsername();
    }

    public Branch saveBranch(Branch branch) {
        // Convert latitude to negative if it's south of the equator
        if (branch.getLatitude() > 0) {
            branch.setLatitude(-branch.getLatitude());
        }

        // Convert longitude to negative if it's west of the Prime Meridian
        if (branch.getLongitude() < 0) {
            branch.setLongitude(-branch.getLongitude());
        }
        return branchRepo.save(branch);
    }

    public Branch addBranch(Branch branch) {
        return branchRepo.save(branch);
    }

    public Branch getBranchById(Long branchId) {
        return branchRepo.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch with ID " + branchId + " not found"));
    }

    public Branch findBranchById(Long branchId) {
        return branchRepo.findById(branchId).orElseThrow(() -> new RuntimeException("Branch not found"));
    }

//    public Branch getBranchById(Long branchId) {
//        Optional<Branch> optionalBranch = branchRepo.findById(branchId);
//        if (optionalBranch.isPresent()) {
//            return optionalBranch.get();
//        } else {
//            throw new EntityNotFoundException("Branch with ID " + branchId + " not found");
//        }
//    }

    public List<Branch> getAllBranches() {
        return branchRepo.findAll();
    }

    // Function to get all bank branch names
    public List<String> getAllBankBranchNames() {
        List<Branch> bankBranches = branchRepo.findAll();
        return bankBranches.stream()
                .map(Branch::getBkBranchName)
                .collect(Collectors.toList());
    }


    // Method to filter branches within a certain radius
    public List<Branch> getBranchesWithinRadius(double userLatitude, double userLongitude, double radius) {
        List<Branch> branches = getAllBranches();
        List<Branch> nearbyBranches = new ArrayList<>();

        for (Branch branch : branches) {
            double distance = calculateDistance(userLatitude, userLongitude, branch.getLatitude(), branch.getLongitude());
            if (distance <= radius) {
                nearbyBranches.add(branch);
            }
        }

        return nearbyBranches;
    }



    // Haversine formula to calculate distance between two points
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the Earth in kilometers
        final double R = 6371.0;

        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate the differences between coordinates
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        System.out.println("**************DISTANCE IS********************"+distance);

        return distance; // Distance in kilometers
    }



}
