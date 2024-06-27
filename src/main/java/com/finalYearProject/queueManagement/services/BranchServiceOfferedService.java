package com.finalYearProject.queueManagement.services;


import com.finalYearProject.queueManagement.model.BankManagerLogin;
import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.repository.BankManagerRepository;
import com.finalYearProject.queueManagement.repository.BranchServiceOfferedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BranchServiceOfferedService {


    @Autowired
    BranchServiceOfferedRepo branchServiceOfferedRepo;

    @Autowired
    private BankManagerRepository bankManagerLoginRepo;

    public BranchServiceOffered savesBranch_Service_offered(BranchServiceOffered branch_service_offered) {
        return branchServiceOfferedRepo.save(branch_service_offered);
    }


    public List<BranchServiceOffered> findAll() {
        return branchServiceOfferedRepo.findAll();
    }

    // Method to fetch bank services by their IDs
    public Set<BranchServiceOffered> getServicesByIds(Set<Long> serviceIds) {
        return branchServiceOfferedRepo.findByIdIn(serviceIds);
    }

    public Set<BranchServiceOffered> getServicesByBranchId(Long branchId) {
        // Retrieve the branch services by branch ID
        return branchServiceOfferedRepo.findByBranchesId(branchId);
    }

    public BranchServiceOffered getServiceById(Long serviceId) {
        return branchServiceOfferedRepo.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Branch service with ID " + serviceId + " not found"));
    }

//    public BranchServiceOffered getServiceById(Long serviceId) {
//        Optional<BranchServiceOffered> optionalBranchServiceOffered = branchServiceOfferedRepo.findById(serviceId);
//        if (optionalBranchServiceOffered.isPresent()) {
//            return optionalBranchServiceOffered.get();
//        } else {
//            throw new EntityNotFoundException("Branch service with ID " + serviceId + " not found");
//        }
//    }


    // Method to fetch all available services offered by the bank
    public List<BranchServiceOffered> getAllServices() {
        return branchServiceOfferedRepo.findAll();
    }

    public List<BranchServiceOffered> getAllBranchServicesOffered() {
        return branchServiceOfferedRepo.findAll();
    }

    public Set<BranchServiceOffered> getOfferedServicesByBranchId(Long branchId) {
        return branchServiceOfferedRepo.findByBranchesId(branchId);
    }


    public void deleteBranch_Service_offered(long id) {
        BranchServiceOffered branch_service_offered = branchServiceOfferedRepo.findById(id).orElse(null);
        if (branch_service_offered != null) {
            branchServiceOfferedRepo.delete(branch_service_offered);
        }

    }

    public Set<BranchServiceOffered> getServicesForManagerBranch(BankManagerLogin manager) {
        return branchServiceOfferedRepo.findByBranches(manager.getBranch());
    }

    public void enableService(Long serviceId, BankManagerLogin manager) {
        Optional<BranchServiceOffered> optionalService = branchServiceOfferedRepo.findById(serviceId);
        if (optionalService.isPresent() && optionalService.get().getBranches().contains(manager.getBranch())) {
            BranchServiceOffered service = optionalService.get();
            service.setEnabled(true);
            branchServiceOfferedRepo.save(service);
        }
    }

    public void disableService(Long serviceId, BankManagerLogin manager) {
        Optional<BranchServiceOffered> optionalService = branchServiceOfferedRepo.findById(serviceId);
        if (optionalService.isPresent() && optionalService.get().getBranches().contains(manager.getBranch())) {
            BranchServiceOffered service = optionalService.get();
            service.setEnabled(false);
            branchServiceOfferedRepo.save(service);
        }
    }



    public Set<BranchServiceOffered> getServicesByBranch(Long branchId) {
        return branchServiceOfferedRepo.findByBranchId(branchId);
    }

    public void AdminUpdateServiceStatus(Long serviceId, boolean enabled) {
        BranchServiceOffered service = getServiceById(serviceId);
        service.setEnabled(enabled);
        branchServiceOfferedRepo.save(service);
    }

}
