package com.finalYearProject.queueManagement.controller;

import com.finalYearProject.queueManagement.model.*;
import com.finalYearProject.queueManagement.services.BranchServiceOfferedService;
import com.finalYearProject.queueManagement.services.BranchServices;
import com.finalYearProject.queueManagement.services.RatingsServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller

public class RatingsController {

    @Autowired
    private BranchServices branchService;

    @Autowired
    private RatingsServices ratingsServices;

    @Autowired
    private BranchServiceOfferedService branchServiceOfferedService;

    private static final Logger logger = LoggerFactory.getLogger(RatingsController.class);


    @GetMapping("/ratingForm")
    public String showRatingForm(Model model) {
        List<Branch> branches = branchService.getAllBranches();
        model.addAttribute("branches", branches);
        return "rating_form";
    }

    @PostMapping("/getOfferedServices")
    @ResponseBody
    public Set<BranchServiceOffered> getOfferedServices(@RequestParam Long branchId) {
        Set<BranchServiceOffered> offeredServices = branchServiceOfferedService.getOfferedServicesByBranchId(branchId);
        return offeredServices;
    }

    // Handle form submission and rating submission
    // Handle form submission and rating submission

    @PostMapping("/submitRating")
    public String submitRating(@ModelAttribute RatingDTO ratingDTO, Model model) {
        logger.debug("Received rating submission: {}", ratingDTO);

        if (ratingDTO.getBranchId() == null || ratingDTO.getServiceId() == null) {
            logger.error("Branch ID or Service ID is null");
            model.addAttribute("error", "Branch ID and Service ID must be provided");
            return "ratingForm"; // return to the form with an error message
        }

        try {
            ratingsServices.saveRating(ratingDTO);
            return "redirect:/rating/success"; // Redirect to a success page or the desired endpoint
        } catch (Exception e) {
            logger.error("Error saving rating", e);
            model.addAttribute("error", "An error occurred while saving the rating");
            return "ratingForm"; // return to the form with an error message
        }

    }

    @GetMapping("/rating/success")
    public String ratingsSuccess(Model model) {
        model.addAttribute("rating-success", new Ratings());
        return "rating-success";
    }
    @GetMapping("/view_ratings")
    public String userGetRatings(Model model) {
        List<Ratings> ratings = ratingsServices.getAllRatings();
        model.addAttribute("ratings", ratings);
        return "readratings";
    }
    @GetMapping("/admin/admin_view_ratings")
    public String adminGetRatings(Model model) {
        List<Ratings> ratings = ratingsServices.getAllRatings();
        model.addAttribute("ratings", ratings);
        return "adminreadratings";
    }

    @PostMapping("/deleterating")
    public String deleteRating(@RequestParam("id") Long id) {
        ratingsServices.deleteRating(id);
        return "redirect:/admin/admin_view_ratings";
    }

}
