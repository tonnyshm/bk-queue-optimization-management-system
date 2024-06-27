//package com.finalYearProject.queueManagement.components;
//
//import com.finalYearProject.queueManagement.model.Branch;
//import com.finalYearProject.queueManagement.model.BranchServiceOffered;
//import com.finalYearProject.queueManagement.repository.BranchRepo;
//import com.finalYearProject.queueManagement.repository.BranchServiceOfferedRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import java.util.Set;
//
//@Component
//public class ServiceInitialization {
//
//    @Autowired
//    private BranchServiceOfferedRepo branchServiceOfferedRepo;
//
//    @Autowired
//    private BranchRepo branchRepo;
//
//    @PostConstruct
//    private void init() {
//        initializeServices();
//    }
//
//    @Transactional
//    public void initializeServices() {
//        Iterable<Branch> branches = branchRepo.findAll();
//
//        for (Branch branch : branches) {
//            // Eagerly fetch the services to avoid LazyInitializationException
//            Set<BranchServiceOffered> services = branch.getBranchServices();
//
//            for (BranchServiceOffered service : services) {
//                service.setEnabled(true);
//                branchServiceOfferedRepo.save(service);
//            }
//        }
//    }
//}
