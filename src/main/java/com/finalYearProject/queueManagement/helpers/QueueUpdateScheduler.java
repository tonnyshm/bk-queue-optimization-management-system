//package com.finalYearProject.queueManagement.helpers;
//
//import com.finalYearProject.queueManagement.model.QueueInformation;
//import com.finalYearProject.queueManagement.repository.QueueInformationRepo;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class QueueUpdateScheduler {
//
//    private final QueueInformationRepo queueInformationRepository;
//
//    public QueueUpdateScheduler(QueueInformationRepo queueInformationRepository) {
//        this.queueInformationRepository = queueInformationRepository;
//    }
//
//    @Scheduled(fixedDelay = 60000) // Run every minute
//    public void updateQueueStatus() {
//        // Get all queue information
//        List<QueueInformation> queueInfos = queueInformationRepository.findAll();
//
//        for (QueueInformation queueInfo : queueInfos) {
//            // Decrement queue size if estimated waiting time has elapsed
//            if (queueInfo.getEstimatedTime() != null && queueInfo.getEstimatedTime().before(new Date())) {
//                int newQueueSize = Math.max(queueInfo.getQueueSize() - 1, 0);
//                queueInfo.setQueueSize(newQueueSize);
//                queueInfo.setEstimatedWaitingHours(newQueueSize * 2);
//                queueInformationRepository.save(queueInfo);
//            }
//        }
//    }
//}
