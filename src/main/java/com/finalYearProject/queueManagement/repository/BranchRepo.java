package com.finalYearProject.queueManagement.repository;

import com.finalYearProject.queueManagement.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepo extends JpaRepository<Branch, Long> {
}
