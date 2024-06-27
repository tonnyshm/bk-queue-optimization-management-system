package com.finalYearProject.queueManagement.repository;
import com.finalYearProject.queueManagement.model.Feedbacks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedbacks, Integer> {
    List<Feedbacks> findByVisibility(boolean visibility);
    List<Feedbacks> findByBranchIdAndVisibility(Long branchId, boolean visibility);

    // Fetch count of feedbacks by branch ID
    Long countByBranchIdAndVisibility(Long branchId, boolean visibility);

}
