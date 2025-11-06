package com.placementportal.placement_website.dto;

import java.util.List;

public class SittingPlanDTO {
    private String assessmentId;
    private List<PlanVenueDTO> venues;

    public SittingPlanDTO() {}
    // getters/setters
    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
    public List<PlanVenueDTO> getVenues() { return venues; }
    public void setVenues(List<PlanVenueDTO> venues) { this.venues = venues; }
}
