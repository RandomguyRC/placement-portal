package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @Column(name = "application_id")
    private String applicationId;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "listing_id")
    private String listingId;

    @Column(name = "willingness")
    private Boolean willingness = true;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "approved")
    private Boolean approved = false;

    @Column(name = "seat_id")
    private String seatId;

    // Getters and Setters
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getListingId() { return listingId; }
    public void setListingId(String listingId) { this.listingId = listingId; }

    public Boolean getWillingness() { return willingness; }
    public void setWillingness(Boolean willingness) { this.willingness = willingness; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }

    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }
}
