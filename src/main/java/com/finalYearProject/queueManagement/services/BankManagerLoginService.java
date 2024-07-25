package com.finalYearProject.queueManagement.services;

import com.finalYearProject.queueManagement.model.BankManagerLogin;
import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.repository.BankManagerRepository;
import com.finalYearProject.queueManagement.repository.BranchServiceOfferedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BankManagerLoginService {
    @Autowired
    private BankManagerRepository bankManagerRepository;

    @Autowired
    private BranchServiceOfferedRepo branchServiceOfferedRepo;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public BankManagerLogin findByUsername(String username) {
        return bankManagerRepository.findByUsername(username);
    }

    public BankManagerLogin save(BankManagerLogin bankManager) {
        // Encode the password before saving
        bankManager.setPassword(passwordEncoder.encode(bankManager.getPassword()));
        return bankManagerRepository.save(bankManager);
    }




    // Service method to get services specific to the manager's branch
    public Set<BranchServiceOffered> getServicesWithBranchStatus(String managerUsername) {
        // Retrieve manager
        BankManagerLogin manager = bankManagerRepository.findByUsername(managerUsername)
               ;

        // Fetch services specific to the manager's branch
        Branch branch = manager.getBranch();
        Set<BranchServiceOffered> branchServices = branch.getBranchServices();

        // Adjust services with status based on manager's settings
        Set<BranchServiceOffered> servicesWithStatus = new HashSet<>();
        for (BranchServiceOffered service : branchServices) {
            boolean isEnabled = manager.getServiceStatus().getOrDefault(service, service.isEnabled());
            BranchServiceOffered serviceWithStatus = new BranchServiceOffered();
            serviceWithStatus.setId(service.getId());
            serviceWithStatus.setName(service.getName());
            serviceWithStatus.setEnabled(isEnabled);
            servicesWithStatus.add(serviceWithStatus);
        }

        return servicesWithStatus;
    }


    public void updateServiceStatus(String managerUsername, Long serviceId, boolean enabled) {
        BankManagerLogin manager = bankManagerRepository.findByUsername(managerUsername);

        BranchServiceOffered service = new BranchServiceOffered();
        service.setId(serviceId);
        manager.getServiceStatus().put(service, enabled);
        bankManagerRepository.save(manager);
    }


}
