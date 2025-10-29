package com.placementportal.placement_website.dto.dto;

public class UpcomingAssessmentDTO {
    private String assessmentId;
    private String type;
    private String startTime;
    private String endTime;
    private String companyName;
    private String jobRole;
    private String modeOfHiring;

    public UpcomingAssessmentDTO() {}

    public UpcomingAssessmentDTO(String assessmentId, String type, String startTime, String endTime,
                                 String companyName, String jobRole, String modeOfHiring) {
        this.assessmentId = assessmentId;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.modeOfHiring = modeOfHiring;
    }

    // Getters and Setters
    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getModeOfHiring() { return modeOfHiring; }
    public void setModeOfHiring(String modeOfHiring) { this.modeOfHiring = modeOfHiring; }
}
