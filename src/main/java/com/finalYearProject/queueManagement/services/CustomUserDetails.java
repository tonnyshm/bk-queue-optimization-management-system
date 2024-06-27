package com.finalYearProject.queueManagement.services;


import com.finalYearProject.queueManagement.model.AdminLogin;
import com.finalYearProject.queueManagement.model.BankManagerLogin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private Object user;
    private String role;

    public CustomUserDetails(Object user, String role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        if (user instanceof BankManagerLogin) {
            return ((BankManagerLogin) user).getPassword();
        } else if (user instanceof AdminLogin) {
            return ((AdminLogin) user).getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (user instanceof BankManagerLogin) {
            return ((BankManagerLogin) user).getUsername();
        } else if (user instanceof AdminLogin) {
            return ((AdminLogin) user).getUsername();
        }
        return null;
    }

    public Object getUser() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}