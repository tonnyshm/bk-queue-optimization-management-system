package com.finalYearProject.queueManagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeedbackDTO {

    private String addDetails;
    private FeedBackAbout about;
    private Long branchId;
    private Long serviceId;
    private boolean visibility;


    // Getters and setters

    public String getAddDetails() {
        return addDetails;
    }

    public void setAddDetails(String addDetails) {
        this.addDetails = addDetails;
    }

    public FeedBackAbout getAbout() {
        return about;
    }

    public void setAbout(FeedBackAbout about) {
        this.about = about;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}


