package com.placementportal.placement_website.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "conducted_at")
@Getter
@Setter
public class ConductedAt {

    @EmbeddedId
    private ConductedAtId id = new ConductedAtId();


    @ManyToOne
    @MapsId("assessmentId")
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @ManyToOne
    @MapsId("venueId")
    @JoinColumn(name = "venue_id")
    private Venue venue;
}