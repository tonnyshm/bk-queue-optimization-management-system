package com.finalYearProject.queueManagement.repository;

import com.finalYearProject.queueManagement.model.AdminLogin;
import com.finalYearProject.queueManagement.model.BankManagerLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository <AdminLogin, Long>{
    AdminLogin findByUsername(String username);
}
