package com.finalYearProject.queueManagement.repository;
import com.finalYearProject.queueManagement.model.BankManagerLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankManagerRepository extends JpaRepository<BankManagerLogin, Long> {
    BankManagerLogin findByUsername(String username);
//    Optional<BankManagerLogin> findByUsername(String username);

    Optional<BankManagerLogin> findByBranchId(Long branchId);

}

