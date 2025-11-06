package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @Column(name = "application_id", nullable = false, length = 255)
    private String applicationId;

    @Column(name = "student_id", length = 255)
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "offer_status")
    private OfferStatus offerStatus;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "offer_date")
    private LocalDate offerDate;

    @Column(name = "location_offered", length = 255)
    private String locationOffered;

    // Getters and Setters
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public OfferStatus getOfferStatus() { return offerStatus; }
    public void setOfferStatus(OfferStatus offerStatus) { this.offerStatus = offerStatus; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public LocalDate getOfferDate() { return offerDate; }
    public void setOfferDate(LocalDate offerDate) { this.offerDate = offerDate; }

    public String getLocationOffered() { return locationOffered; }
    public void setLocationOffered(String locationOffered) { this.locationOffered = locationOffered; }

    // Enum for offer status
    public enum OfferStatus {
        YES, NO
    }
}
