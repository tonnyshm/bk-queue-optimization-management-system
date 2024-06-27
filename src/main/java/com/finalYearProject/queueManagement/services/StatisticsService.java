package com.finalYearProject.queueManagement.services;

import com.finalYearProject.queueManagement.model.BookedUserInfo;
import com.finalYearProject.queueManagement.repository.BookUserInfoRepo;
import com.finalYearProject.queueManagement.repository.FeedbackRepository;
import com.finalYearProject.queueManagement.repository.RatingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private BookUserInfoRepo bookedUserInfoRepository;

    @Autowired
    private RatingsRepo ratingsRepository;

    @Autowired
    private FeedbackRepository feedbacksRepository;

    public Map<String, Long> getMostSoughtServices(Long branchId) {
        List<BookedUserInfo> bookings = bookedUserInfoRepository.findByBranchId(branchId);
        return bookings.stream()
                .collect(Collectors.groupingBy(info -> info.getService().getName(), Collectors.counting()));
    }

    public long getDailyVisitors(Long branchId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return bookedUserInfoRepository.countByBranchIdAndUserVisitedDateBetween(branchId, startOfDay, endOfDay);
    }

    public long getTotalRatings(Long branchId) {
        return ratingsRepository.countByBranchId(branchId);
    }

    public long getTotalFeedbacks(Long branchId) {
        return feedbacksRepository.countByBranchIdAndVisibility(branchId, false);
    }

    public List<Object[]> getPeakHours(Long branchId) {
        return bookedUserInfoRepository.findPeakHours(branchId);
    }


}
