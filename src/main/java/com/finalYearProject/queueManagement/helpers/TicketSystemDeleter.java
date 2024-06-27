//package com.finalYearProject.queueManagement.helpers;
//
//import com.finalYearProject.queueManagement.repository.TicketRepo;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Component
//public class TicketSystemDeleter {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Autowired
//    private TicketRepo ticketRepo; // Replace YourTableRepository with your actual repository
//
//    @Transactional
//    @Scheduled(cron = "0 0 6 * * *") // Schedule to run at 6 AM every day
//    public void deleteTableData() {
//        try {
//            entityManager.createNativeQuery("TRUNCATE TABLE ticket_system").executeUpdate(); // Replace your_table_name with the name of your table
//            // You can also use other ways like entityManager.remove() if you want to delete specific entities instead of truncating the whole table.
//            ticketRepo.flush(); // Flush to apply the changes immediately
//            System.out.println("Ticket Table data deleted successfully at 6 AM.");
//        } catch (Exception ex) {
//            System.err.println("Error occurred while deleting Ticket table data: " + ex.getMessage());
//        }
//    }
//}
