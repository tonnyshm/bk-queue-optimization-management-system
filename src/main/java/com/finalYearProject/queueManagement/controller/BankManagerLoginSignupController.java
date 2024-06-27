package com.finalYearProject.queueManagement.controller;

import com.finalYearProject.queueManagement.model.BankManagerLogin;
import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.UserRole;
import com.finalYearProject.queueManagement.repository.BranchRepo;
import com.finalYearProject.queueManagement.services.BankManagerLoginService;
import com.finalYearProject.queueManagement.services.BranchServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
//@RequestMapping("/bank-manager")
public class BankManagerLoginSignupController {

    @Autowired
    private BankManagerLoginService bankManagerService;
    @Autowired
    private BranchServices bankBranchService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BranchRepo branchRepo;


    @GetMapping("/bank-manager/bank_manager_login")
    public String BankManagerLoginPage() {
        return "bank_manager_login";
    }



    @GetMapping("/bank-manager/perform_login")
    public String BankManagerLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        System.out.println("__///___///////_______++++ USERNAME IS:_____///|||___||/////__:"+username);
        return "redirect:/bank-manager/bank_manager/home"; // Redirect to bank Manager home page after successful login
    }



        @GetMapping("/bank-manager/bank_manager/home")
        public String BankManagerHomePage(Model model) {
            // Get the authenticated user's details
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                System.out.println("//////////++++ USERNAME IS:///|||||/////:"+username);
                // Add the username to the model
                model.addAttribute("username", username);
            }
            return "bank_manager_home";
        }





    @GetMapping("/admin/bank_manager_signup")
    public String showSignupPage(Model model) {
        List<Branch> branches = branchRepo.findAll();
        model.addAttribute("bankManager", new BankManagerLogin());
        model.addAttribute("bankBranches", bankBranchService.getAllBankBranchNames()); // Fetch bank branch names
        model.addAttribute("bankBranches", branches);
        return "bank_manager_signup"; // Assuming you have a signup.html template
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("bankManager") BankManagerLogin bankManager,
                         @RequestParam("branchId") Long branchId) {
        // Fetch the Branch entity using the branchId
        Branch branch = bankBranchService.findBranchById(branchId);


        // Set the branch for the bank manager
        bankManager.setBranch(branch);

        // Set the role for the bank manager
        bankManager.setRole(UserRole.BANK_MANAGER);

        // Save the bank manager
        bankManagerService.save(bankManager);

        // Redirect to login page after signup
        return "redirect:/admin/system_admin/home";
    }

//    @GetMapping("/logout")
//    public String managerLogoutPage() {
//        return "/perform_logout";
//    }


    @GetMapping("/ ")
    public RedirectView logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false); // Get session if it exists, don't create new one
        if (session != null) {
            session.invalidate(); // Invalidate the session
        }
        return new RedirectView("/home"); // Redirect to the home page
    }

}
