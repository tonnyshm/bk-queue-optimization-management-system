package com.finalYearProject.queueManagement.services;

import com.finalYearProject.queueManagement.model.BookedUserInfo;
import com.finalYearProject.queueManagement.model.Branch;
import com.finalYearProject.queueManagement.model.BranchServiceOffered;
import com.finalYearProject.queueManagement.model.Ratings;
import com.finalYearProject.queueManagement.repository.BookUserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookedUserInfoService {

    @Autowired
    private BookUserInfoRepo bookUserInfoRepo;


    public void saveUserBookedRecord(String userEmail, Branch branch, BranchServiceOffered service) {
        BookedUserInfo bookedUserInfo = new  BookedUserInfo();
        bookedUserInfo.setUserEmail(userEmail);
        bookedUserInfo.setBranch(branch);
        bookedUserInfo.setService(service);
        bookedUserInfo.setUserVisitedDate(LocalDateTime.now());
        bookUserInfoRepo.save(bookedUserInfo);
    }


    public void deleteBookedUserInfo(Long id) {
        BookedUserInfo bookedUserInfo = bookUserInfoRepo.findById(id).orElse(null);
        if (bookedUserInfo != null) {
            bookUserInfoRepo.delete(bookedUserInfo);
        }
    }


    public List<BookedUserInfo> getBookedUsersByBranchAndDateRange(Long branchId, LocalDateTime startDate, LocalDateTime endDate) {
        return bookUserInfoRepo.findByBranchIdAndUserVisitedDateBetween(branchId, startDate, endDate);
    }

}
