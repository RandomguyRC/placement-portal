package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @Column(name = "application_id", length = 255)
    private String applicationId;

    @Column(name = "student_id", length = 255)
    private String studentId;

    @Column(name = "listing_id", length = 255)
    private String listingId;

    @Column(name = "willingness")
    private boolean willingness;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "applied_at")
    private Date appliedAt;

    @Column(name = "approved")
    private boolean approved;

    @Column(name = "seat_id", length = 50)
    private String seatId;

    // 🔹 Constructors
    public Application() {}

    public Application(String applicationId, String studentId, String listingId, boolean willingness, Date appliedAt, boolean approved, String seatId) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.listingId = listingId;
        this.willingness = willingness;
        this.appliedAt = appliedAt;
        this.approved = approved;
        this.seatId = seatId;
    }

    // 🔹 Getters and Setters
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public boolean isWillingness() {
        return willingness;
    }

    public void setWillingness(boolean willingness) {
        this.willingness = willingness;
    }

    public Date getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Date appliedAt) {
        this.appliedAt = appliedAt;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }
}
