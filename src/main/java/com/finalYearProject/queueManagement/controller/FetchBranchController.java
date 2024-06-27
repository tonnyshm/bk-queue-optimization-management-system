package com.finalYearProject.queueManagement.controller;

import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.services.BranchServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class FetchBranchController {

        @Autowired
        private BranchServices branchServices;

        @GetMapping("/nearby")
        public String getNearbyPage(@RequestParam(required = false) Double userLatitude,
                                    @RequestParam(required = false) Double userLongitude,
                                    @RequestParam(required = false) Double radius,
                                    Model model) {
                if (userLatitude != null && userLongitude != null && radius != null) {
                        // User has provided location parameters, fetch nearby branches
                        List<Branch> nearbyBranches = branchServices.getBranchesWithinRadius(userLatitude, userLongitude, radius);
                        model.addAttribute("branches", nearbyBranches);
                        return "nearby_branches";
                } else {
                        // User has not provided location parameters, show location selection form
                        return "select_location";
                }
        }
}




