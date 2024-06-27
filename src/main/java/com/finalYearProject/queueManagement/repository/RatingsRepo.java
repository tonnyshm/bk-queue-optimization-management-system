package com.finalYearProject.queueManagement.repository;

import com.finalYearProject.queueManagement.model.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RatingsRepo extends JpaRepository<Ratings, Long> {
    List<Ratings> findByBranchId(Long branchId);
    // Fetch count of ratings by branch ID
    Long countByBranchId(Long branchId);
}
