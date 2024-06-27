package com.finalYearProject.queueManagement.controller;

import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class Home {


    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("landing", new Branch());
        return "landing";
    }

    @GetMapping("/home")
    public String homePage2(Model model) {
        model.addAttribute("landing", new Branch());
        return "landing";
    }

    @GetMapping("/service-we-offer")
    public String serviceWeOfferPage() {
        return "service_we_offer";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about_page";
    }



    @GetMapping("/booking_confirmation")
    public String bookingConfirmation(Model model) {
        model.addAttribute("booking_confirmation", new Branch());
        return "booking_confirmation";
    }


}
