package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a student's application for a particular job listing.
 */
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @Column(name = "application_id", length = 255, nullable = false)
    private String applicationId;

    @Column(name = "student_id", length = 255, nullable = false)
    private String studentId;

    @Column(name = "listing_id", length = 255, nullable = false)
    private String listingId;

    @Column(name = "willingness")
    private Boolean willingness = true; // default true, since student applies willingly

    @Column(name = "applied_at")
    private LocalDateTime appliedAt = LocalDateTime.now(); // set automatically on creation

    @Column(name = "approved")
    private Boolean approved = false;

    @Column(name = "seat_id", length = 50)
    private String seatId;

    // 🔹 Constructors
    public Application() {}

    public Application(String applicationId, String studentId, String listingId,
                       Boolean willingness, LocalDateTime appliedAt,
                       Boolean approved, String seatId) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.listingId = listingId;
        this.willingness = willingness != null ? willingness : true;
        this.appliedAt = appliedAt != null ? appliedAt : LocalDateTime.now();
        this.approved = approved != null ? approved : false;
        this.seatId = seatId;
    }

    // 🔹 Getters & Setters
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