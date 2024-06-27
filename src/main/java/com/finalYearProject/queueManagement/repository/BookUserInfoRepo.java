package com.finalYearProject.queueManagement.repository;

import com.finalYearProject.queueManagement.model.BookedUserInfo;
import com.finalYearProject.queueManagement.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookUserInfoRepo extends JpaRepository<BookedUserInfo, Long> {
    @Query("SELECT b FROM BookedUserInfo b WHERE b.userVisitedDate BETWEEN :startTime AND :endTime "
            + "AND (:branchId IS NULL OR b.branch.id = :branchId) "
            + "AND (:serviceId IS NULL OR b.service.id = :serviceId)")
    List<BookedUserInfo> findUsersByVisitTimeRangeAndFilters(@Param("startTime") LocalDateTime startTime,
                                                             @Param("endTime") LocalDateTime endTime,
                                                             @Param("branchId") Long branchId,
                                                             @Param("serviceId") Long serviceId);

    List<BookedUserInfo> findByBranchIdAndUserVisitedDateBetween(Long branchId, LocalDateTime startDate, LocalDateTime endDate);

    List<BookedUserInfo> findByBranchId(Long branchId);

    Long countByBranchIdAndUserVisitedDateBetween(Long branchId, LocalDateTime startDate, LocalDateTime endDate);


    @Query("SELECT HOUR(b.userVisitedDate) as hour, COUNT(b) as count FROM BookedUserInfo b WHERE b.branch.id = :branchId GROUP BY HOUR(b.userVisitedDate) ORDER BY count(b) DESC")
    List<Object[]> findPeakHours(@Param("branchId") Long branchId);



    // Count users visited between specific dates
//    @Query("SELECT COUNT(b) FROM BookedUserInfo b WHERE b.userVisitedDate BETWEEN :startDate AND :endDate")
//    long countByUserVisitedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Most visited branches
    @Query("SELECT b.bkBranchName, COUNT(bu.id) FROM BookedUserInfo bu JOIN bu.branch b GROUP BY b.bkBranchName ORDER BY COUNT(bu.id) DESC")
    List<Object[]> findMostVisitedBranches();

    @Query("SELECT s.name, COUNT(bu.id) FROM BookedUserInfo bu JOIN bu.service s GROUP BY s.name ORDER BY COUNT(bu.id) DESC")
    List<Object[]> findMostSoughtServices();

    @Query("SELECT HOUR(bu.userVisitedDate), COUNT(bu.id) FROM BookedUserInfo bu GROUP BY HOUR(bu.userVisitedDate) ORDER BY HOUR(bu.userVisitedDate)")
    List<Object[]> findPeakHours();

    long countByUserVisitedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}




