package com.finalYearProject.queueManagement.controller;

import com.finalYearProject.queueManagement.model.*;
import com.finalYearProject.queueManagement.repository.FeedbackRepository;
import com.finalYearProject.queueManagement.services.BranchServiceOfferedService;
import com.finalYearProject.queueManagement.services.BranchServices;
import com.finalYearProject.queueManagement.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private BranchServices branchService;

    @Autowired
    private BranchServiceOfferedService branchServiceOfferedService;

    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        List<Branch> branches = branchService.getAllBranches();
        List<BranchServiceOffered> services = branchServiceOfferedService.getAllServices();
        FeedBackAbout[] feedbackAbout = FeedBackAbout.values();

        model.addAttribute("branches", branches);
        model.addAttribute("services", services);
        model.addAttribute("feedbackAbout", feedbackAbout);

        return "usersfeedbackform";  //  the name of my HTML template
    }

    @PostMapping("/feedback/submit")
    public String submitFeedback(@ModelAttribute FeedbackDTO feedbackDTO, Model model) {
        // Process the feedbackDTO here
        feedbackService.saveFeedback(feedbackDTO);
        return "redirect:/feedback/success";
    }

    @GetMapping("/feedback/success")
    public String feedBackSuccess(Model model) {
        model.addAttribute("feedback-success", new Feedbacks());
        return "feedback-success";
    }

    @GetMapping("/view_feedbacks")
    public String getVisibleFeedbacks(Model model) {
        List<Feedbacks> feedbacks = feedbackRepository.findByVisibility(true);
        model.addAttribute("feedbacks", feedbacks);
        return "readfeedbacks";
    }

    @GetMapping("/admin/admin_read_feedbacks")
    public String readFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "adminreadfeedbacks";
    }

    @PostMapping("/deletefeedback")
    public String deleteFeedback(@RequestParam int id) {
        feedbackService.deleteFeedback(id);
        return "redirect:/admin/admin_read_feedbacks";
    }


}

