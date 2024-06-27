package com.finalYearProject.queueManagement.controller;

import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.services.BranchServiceOfferedService;
import com.finalYearProject.queueManagement.services.BranchServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class RegisterBranches {

    @Autowired
    private BranchServices branchServices;

    @Autowired
    private BranchServiceOfferedService branchServiceOfferedService;


    @GetMapping("/admin/bkbranch_services")
    public String bankBranch_services(Model model) {
        model.addAttribute("services", branchServiceOfferedService.getAllServices());
        model.addAttribute("BranchServiceOffered", new BranchServiceOffered());
        return "branch_offered_services";
    }




//    @GetMapping("/Registerbranches")
//    public String showRegisterBranchesForm(Model model) {
//        model.addAttribute("Branch", new Branch());
//        return "Registerbranches";
//    }
//    @PostMapping("/branches/register")
//    public String registerBranch(@ModelAttribute("Registerbranches") Branch branch) {
//        branchServices.saveBranch(branch);
//        // Redirect to a success page or display a message
//        return "redirect:/";
//    }





    // Mapping to show the registration form for branches
    @GetMapping("/admin/Registerbranches")
    public String showRegisterBranchesForm(Model model) {
        // Fetch all available services offered by the bank
        List<BranchServiceOffered> allServices = branchServiceOfferedService.getAllServices();

        // Add the list of services to the model
        model.addAttribute("allServices", allServices);

        // Add a new Branch object to the model for form binding
        model.addAttribute("Branch", new Branch());

        return "Registerbranches"; // Return the name of the Thymeleaf template
    }

    // Handling the form submission to register a new branch
    @PostMapping("/branches/register")
    public String registerBranch(@ModelAttribute("branch") Branch branch,
                                 @RequestParam(value = "branchServices", required = false) Set<Long> serviceIds) {
        System.out.println("+++++++++++THIS IS +++++++++++++++"+serviceIds);
        // If any services are selected, fetch them from the database and add them to the branch object
        if (serviceIds != null && !serviceIds.isEmpty()) {
            Set<BranchServiceOffered> services = branchServiceOfferedService.getServicesByIds(serviceIds);
            branch.setBranchServices(services);
        }

        // Save the branch object
        branchServices.saveBranch(branch);

        // Redirect to a success page or display a message
        return "redirect:/admin/system_admin/home";
    }

//    @PostMapping("/branches/register")
//    public String registerBranch(@ModelAttribute("branch") Branch branch,
//                                 @RequestParam(value = "branchServices", required = false) String[] serviceIds) {
//        if (serviceIds != null && serviceIds.length > 0) {
//            Set<BranchServiceOffered> services = new HashSet<>();
//            for (String id : serviceIds) {
//                branchServiceOfferedRepo.findById(Long.parseLong(id)).ifPresent(services::add);
//            }
//            branch.setBranchServices(services);
//        }
//
//        branchServices.saveBranch(branch);
//        return "redirect:/";
//    }





    // Mapping to display the form page
    @GetMapping("/branch_services")
    public String show_branch_service_offeredPage(Model model) {
        model.addAttribute("BranchServiceOffered", branchServiceOfferedService.findAll());
        model.addAttribute("newService", new BranchServiceOffered());
        return "branch_services";
    }

    // Mapping to add a new service
    @PostMapping("/branch_services")
    public String add_branch_service_offered(@ModelAttribute("BranchServiceOffered") BranchServiceOffered branch_service_offered) {
        branchServiceOfferedService.savesBranch_Service_offered(branch_service_offered);
        return "redirect:/admin/bkbranch_services";
    }

    // Mapping to delete a service
    @PostMapping("/branch_services/delete")
    public String delete_branch_service_offered(@RequestParam("id") long id) {
        branchServiceOfferedService.deleteBranch_Service_offered(id);
        return "redirect:/admin/bkbranch_services";
    }



}