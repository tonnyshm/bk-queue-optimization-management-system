package com.finalYearProject.queueManagement.services;

import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.model.RatingDTO;
import com.finalYearProject.queueManagement.model.Ratings;
import com.finalYearProject.queueManagement.repository.BranchRepo;
import com.finalYearProject.queueManagement.repository.BranchServiceOfferedRepo;
import com.finalYearProject.queueManagement.repository.RatingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingsServices {

    @Autowired
    private RatingsRepo ratingsRepo;

    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private BranchServiceOfferedRepo branchServiceOfferedRepository;


    public void saveRating(RatingDTO ratingDTO) {
        Ratings rating = new Ratings();
        rating.setComment(ratingDTO.getComment());
        rating.setRatingValue(ratingDTO.getRatingValue());

        Branch branch = branchRepo.findById(ratingDTO.getBranchId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid branch ID: " + ratingDTO.getBranchId()));
        rating.setBranch(branch);

        BranchServiceOffered service = branchServiceOfferedRepository.findById(ratingDTO.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid service ID: " + ratingDTO.getServiceId()));
        rating.setBranch_Service_offered(service);

        ratingsRepo.save(rating);
    }


    public List<Ratings> findAll() {
        return ratingsRepo.findAll();
    }

    public List<Ratings> getAllRatings() {
        return ratingsRepo.findAll();
    }


    public void deleteRatings(Long id) {
        Ratings ratings = ratingsRepo.findById(id).orElse(null);
        if (ratings != null) {
            ratingsRepo.delete(ratings);
        }

    }

    public List<Ratings> getRatingsByBranchId(Long branchId) {
        return ratingsRepo.findByBranchId(branchId);
    }

    public void deleteRating(Long id) {
        ratingsRepo.deleteById(id);
    }
}