package com.finalYearProject.queueManagement.services;

import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.model.FeedbackDTO;
import com.finalYearProject.queueManagement.model.Feedbacks;
import com.finalYearProject.queueManagement.repository.BranchRepo;
import com.finalYearProject.queueManagement.repository.BranchServiceOfferedRepo;
import com.finalYearProject.queueManagement.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private BranchRepo branchRepository;

    @Autowired
    private BranchServiceOfferedRepo branchServiceOfferedRepository;

    public void saveFeedback(FeedbackDTO feedbackDTO) {
        Feedbacks feedback = new Feedbacks();
        feedback.setAdd_details(feedbackDTO.getAddDetails());
        feedback.setAbout(feedbackDTO.getAbout());
        feedback.setVisibility(feedbackDTO.isVisibility());

        Branch branch = branchRepository.findById(feedbackDTO.getBranchId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid branch ID"));
        feedback.setBranch(branch);

        BranchServiceOffered branchServiceOffered = branchServiceOfferedRepository.findById(feedbackDTO.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid service ID"));
        feedback.setBranch_Service_offered(branchServiceOffered);

        feedbackRepository.save(feedback);
    }

    public void deleteFeedback(int id) {
        feedbackRepository.deleteById(id);
    }

    public List<Feedbacks> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public List<Feedbacks> getFeedbacksByBranchIdAndVisibility(Long branchId, boolean visibility) {
        return feedbackRepository.findByBranchIdAndVisibility(branchId, visibility);
    }
}

