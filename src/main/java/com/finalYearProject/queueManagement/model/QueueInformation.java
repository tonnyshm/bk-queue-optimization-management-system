package com.finalYearProject.queueManagement.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class QueueInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private BranchServiceOffered service;

    @Column(name = "estimated_waiting_hours")
    private int estimatedWaitingHours;

    @Column(name = "queue_size")
    private int queueSize;

    @Column(name = "estimated_time")
    private Date estimatedTime; //used for displaying your scheduled hours

    // Other attributes such as timestamp, date, etc.
}
