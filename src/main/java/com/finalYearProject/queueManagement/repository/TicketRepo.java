package com.finalYearProject.queueManagement.repository;

import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.model.TicketSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<TicketSystem, Long> {
    List<TicketSystem> findByServiceOrderByTicketNumber(BranchServiceOffered service);

}
