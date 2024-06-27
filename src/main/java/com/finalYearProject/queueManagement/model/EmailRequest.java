package com.finalYearProject.queueManagement.model;

public class EmailRequest {
    private String timeRange;
    private String subject;
    private String message;
    private Long branchId;
    private Long serviceId;

    public String getTimeRange() {
        return timeRange;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public Long getBranchId() {
        return branchId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}

