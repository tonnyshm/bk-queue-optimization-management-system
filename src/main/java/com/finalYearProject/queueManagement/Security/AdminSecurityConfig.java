package com.finalYearProject.queueManagement.Security;

import com.finalYearProject.queueManagement.services.AdminUserDetailsService;
import com.finalYearProject.queueManagement.services.BankManagerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/admin/**")
                .authorizeRequests()
                .antMatchers("/static/**","/images/**", "/favicon.ico","/admin_signup",
                        "/templates/1680506194279.ico","/feedback","/admin/ratings-feedbacks").permitAll()
                .antMatchers("/admin/system_admin/home","/admin/pause/services",
                        "/admin/statistics","/admin/admin_read_feedbacks","/admin/admin_view_ratings",
                        "/admin/bank_manager_signup", "/admin/bkbranch_services",
                        "/admin/Registerbranches","/admin/sendEmailsForm","/admin/report").authenticated()
                .anyRequest().hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/admin/admin_login")
                .loginProcessingUrl("/admin/admin_perform_login")
                .defaultSuccessUrl("/admin/system_admin/home", true)
                .failureUrl("/admin/admin_login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminUserDetailsService).passwordEncoder(passwordEncoder);
    }
}

