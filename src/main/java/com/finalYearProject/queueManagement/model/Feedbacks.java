package com.finalYearProject.queueManagement.model;



import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Feedbacks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    private String add_details;
    @Enumerated(EnumType.STRING)
    private FeedBackAbout about;

    private String BranchName;
    private boolean visibility;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonBackReference
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonBackReference
    private BranchServiceOffered branch_Service_offered;

}
