package com.finalYearProject.queueManagement.repository;

import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BranchServiceOfferedRepo extends JpaRepository<BranchServiceOffered, Long> {

    Set<BranchServiceOffered> findByIdIn(Set<Long> ids);
    Set<BranchServiceOffered> findByBranchesId(Long branchId);

    Set<BranchServiceOffered> findByBranches(Branch branch);

    @Query("SELECT bso FROM BranchServiceOffered bso JOIN bso.branches b WHERE b.id = :branchId")
    Set<BranchServiceOffered> findByBranchId(@Param("branchId") Long branchId);

    @Query("SELECT bso FROM BranchServiceOffered bso JOIN bso.branches b WHERE bso.id = :serviceId AND b.id = :branchId")
    Optional<BranchServiceOffered> findByIdAndBranchId(@Param("serviceId") Long serviceId, @Param("branchId") Long branchId);
}
