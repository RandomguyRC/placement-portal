package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sitting_plan")
public class SittingPlan {

    @Id
    @Column(name = "plan_id", length = 255)
    private String planId;

    @Column(name = "assessment_id", unique = true, length = 255)
    private String assessmentId;

    @Lob
    @Column(name = "plan_json", columnDefinition = "LONGTEXT")
    private String planJson;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public SittingPlan() {}

    public SittingPlan(String planId, String assessmentId, String planJson, LocalDateTime createdAt) {
        this.planId = planId;
        this.assessmentId = assessmentId;
        this.planJson = planJson;
        this.createdAt = createdAt;
    }

    // getters & setters
    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }
    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
    public String getPlanJson() { return planJson; }
    public void setPlanJson(String planJson) { this.planJson = planJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}