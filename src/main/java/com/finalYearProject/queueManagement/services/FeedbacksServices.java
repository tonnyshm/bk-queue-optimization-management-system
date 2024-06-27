package com.finalYearProject.queueManagement.services;


import com.finalYearProject.queueManagement.model.Feedbacks;
import com.finalYearProject.queueManagement.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbacksServices {


    @Autowired
    private FeedbackRepository feedbackRepository;



    public Feedbacks savefeedbacks (Feedbacks feedbacks){
        return feedbackRepository.save(feedbacks);
    }


    public List<Feedbacks> findAll() {
        return feedbackRepository.findAll();
    }


    public void deleteFeedbacks(int id){
        Feedbacks feedbacks = feedbackRepository.findById(id).orElse(null);
        if(feedbacks !=null){
            feedbackRepository.delete(feedbacks);
        }
    }



}
