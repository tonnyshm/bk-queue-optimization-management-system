package com.finalYearProject.queueManagement.model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class TicketSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketNumber;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private BranchServiceOffered service;
}
