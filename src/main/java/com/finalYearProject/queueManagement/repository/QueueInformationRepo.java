package com.finalYearProject.queueManagement.repository;

import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.QueueInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueInformationRepo extends JpaRepository<QueueInformation, Long> {

    QueueInformation findByBranchId(Long branchId);
    Optional<QueueInformation> findByBranch(Branch branch);

    Optional<QueueInformation> findByBranchIdAndServiceId(Long branchId, Long serviceId);

//    List<QueueInformation> findByBranchIdAndServiceId(Long branchId, Long serviceId);
  QueueInformation findTopByBranchId(Long branchId);

}
