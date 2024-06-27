package com.finalYearProject.queueManagement.services;


import com.finalYearProject.queueManagement.model.AdminLogin;
import com.finalYearProject.queueManagement.model.BankManagerLogin;
import com.finalYearProject.queueManagement.repository.AdminRepository;
import com.finalYearProject.queueManagement.repository.BankManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BankManagerUserDetailsService implements UserDetailsService {

    @Autowired
    private BankManagerRepository bankManagerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BankManagerLogin bankManager = bankManagerRepository.findByUsername(username);
        if (bankManager == null) {
            throw new UsernameNotFoundException("Bank Manager not found with username: " + username);
        }
        return new CustomUserDetails(bankManager, "BANK_MANAGER");
    }
}


