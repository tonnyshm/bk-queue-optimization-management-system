package com.finalYearProject.queueManagement.services;

import com.finalYearProject.queueManagement.model.AdminLogin;
import com.finalYearProject.queueManagement.model.UserRole;
import com.finalYearProject.queueManagement.repository.AdminRepository;
import com.finalYearProject.queueManagement.repository.BookUserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminEmailService emailService;

    @Autowired
    private BookUserInfoRepo bookedUserInfoRepository;

    public AdminLogin createAdmin(String username) {
        String generatedPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(generatedPassword);

        AdminLogin admin = new AdminLogin();
        admin.setUsername(username);
        admin.setPassword(encodedPassword);
        admin.setRole(UserRole.ADMIN);

        AdminLogin savedAdmin = adminRepository.save(admin);

        // Send email with the generated password
        emailService.sendSimpleMessage("tonny.shema@gmail.com", "Admin Account Created",
                "Your account has been created. Your password is: " + generatedPassword);

        return savedAdmin;
    }

    private String generateRandomPassword() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public List<Object[]> getMostVisitedBranches() {
        return bookedUserInfoRepository.findMostVisitedBranches();
    }

    public List<Object[]> getMostSoughtServices() {
        return bookedUserInfoRepository.findMostSoughtServices();
    }

    public List<Object[]> getPeakHours() {
        return bookedUserInfoRepository.findPeakHours();
    }

    public long getDailyVisitors() {
        LocalDate today = LocalDate.now();
        return bookedUserInfoRepository.countByUserVisitedDateBetween(
                today.atStartOfDay(), today.atTime(23, 59, 59));
    }

    public long getWeeklyVisitors() {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return bookedUserInfoRepository.countByUserVisitedDateBetween(
                startOfWeek.atStartOfDay(), endOfWeek.atTime(23, 59, 59));
    }

    public long getMonthlyVisitors() {
        LocalDate startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());
        return bookedUserInfoRepository.countByUserVisitedDateBetween(
                startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59));
    }

}
