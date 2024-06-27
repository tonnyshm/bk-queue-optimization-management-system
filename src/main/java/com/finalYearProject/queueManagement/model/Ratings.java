package com.finalYearProject.queueManagement.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Ratings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String comment;
    private int ratingValue;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private BranchServiceOffered branch_Service_offered;
}
