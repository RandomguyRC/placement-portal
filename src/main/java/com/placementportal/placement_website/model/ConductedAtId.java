package com.placementportal.placement_website.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConductedAtId implements Serializable {

    private String assessmentId;
    private String venueId;

    // ✅ Default constructor (required by JPA)
    public ConductedAtId() {}

    // ✅ Custom constructor for manual creation
    public ConductedAtId(String assessmentId, String venueId) {
        this.assessmentId = assessmentId;
        this.venueId = venueId;
    }

    // ✅ Getters & setters (you can also use Lombok if you like)
    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    // ✅ equals() and hashCode() — critical for composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConductedAtId)) return false;
        ConductedAtId that = (ConductedAtId) o;
        return Objects.equals(assessmentId, that.assessmentId)
                && Objects.equals(venueId, that.venueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentId, venueId);
    }
}