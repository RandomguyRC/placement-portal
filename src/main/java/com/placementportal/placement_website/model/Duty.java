package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "duties")
@Getter
@Setter
public class Duty {

    @Id
    @Column(name = "duty_id", nullable = false, length = 255)
    private String dutyId;

    // Many duties can be assigned to the same TPR
    @ManyToOne
    @JoinColumn(name = "tpr_id", referencedColumnName = "tpr_id")
    private Tpr tpr;

    // Many duties can be for the same venue
    @ManyToOne
    @JoinColumn(name = "venue_id", referencedColumnName = "venue_id")
    private Venue venue;

    // Each duty is related to a particular job listing
    @ManyToOne
    @JoinColumn(name = "related_listing_id", referencedColumnName = "listing_id")
    private JobListing relatedListing;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DutyStatus status;  // ✅ Enum defined below

    // ✅ Add this enum definition
    public enum DutyStatus {
        MISSED,
        COMPLETED,
        UPCOMING
    }
}
