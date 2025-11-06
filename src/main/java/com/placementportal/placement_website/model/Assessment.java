package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Assessment {

    @Id
    private String assessmentId;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private JobListing jobListing;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    // Constructors
    public Assessment() {}

    // Getters and Setters
    public String getAssessmentId() {
        return assessmentId;
    }
    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public JobListing getJobListing() {
        return jobListing;
    }
    public void setJobListing(JobListing jobListing) {
        this.jobListing = jobListing;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
