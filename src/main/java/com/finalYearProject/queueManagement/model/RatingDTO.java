package com.finalYearProject.queueManagement.model;

public class RatingDTO {
    private Long branchId;
    private Long serviceId;
    private String comment;
    private int ratingValue;

    // Getters and Setters

    public Long getBranchId() {
        return branchId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getComment() {
        return comment;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    @Override
    public String toString() {
        return "RatingDTO{" +
                "branchId=" + branchId +
                ", serviceId=" + serviceId +
                ", comment='" + comment + '\'' +
                ", ratingValue=" + ratingValue +
                '}';
    }
}
