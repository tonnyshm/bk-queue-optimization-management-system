package com.finalYearProject.queueManagement.Security;

import com.finalYearProject.queueManagement.services.BankManagerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(2)
public class BankManagerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BankManagerUserDetailsService bankManagerUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/bank-manager/**")
                .authorizeRequests()
                .antMatchers("/view_feedbacks","/service-we-offer","/about","/view_ratings","/admin_view_ratings",
                        "admin_read_feedbacks","/feedback/success", "/static/**","/images/**",
                        "/favicon.ico","/templates/1680506194279.ico", "/webjars/**", "/signup", "/", "/home",
                        "/nearby","/feedback", "/ratingForm", "/branch_services", "/branches/register", "/queue-management/{id}","/perform_logout",
                        "/booking_confirmation", "/cancel-appointment","/service-unavailable","/admin-stats","/perform_logout").permitAll()
                .antMatchers("/bank-manager/pause-services","/bank-manager/services",
                        "/bank-manager/bank_manager/home","/bank-manager/manager/ratings",
                        "/bank-manager/ratings-feedback","/bank-manager/manager/feedbacks",
                        "/bank-manager/report","/bank-manager/generate-report",
                        "/bank-manager/manager/statistics","/bank-manager/view-queue").authenticated()
                .anyRequest().hasRole("BANK_MANAGER")
                .and()
                .formLogin()
                .loginPage("/bank-manager/bank_manager_login")
                .loginProcessingUrl("/bank-manager/perform_login")
                .defaultSuccessUrl("/bank-manager/bank_manager/home", true)
                .failureUrl("/bank_manager_login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(bankManagerUserDetailsService).passwordEncoder(passwordEncoder);
    }
}


