package com.placementportal.placement_website.model;

import java.sql.Timestamp;

public class Assessment {
    private String assessmentId;
    private String type;
    private Timestamp startTime;
    private Timestamp endTime;
    private String listingId;
    private String seatingPlan;

    // Getters and Setters
    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public String getListingId() { return listingId; }
    public void setListingId(String listingId) { this.listingId = listingId; }

    public String getSeatingPlan() { return seatingPlan; }
    public void setSeatingPlan(String seatingPlan) { this.seatingPlan = seatingPlan; }
}
