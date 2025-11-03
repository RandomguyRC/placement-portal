package com.placementportal.placement_website.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class Assessment {
    @Id
    private String assessmentId;

    @ManyToOne
    @JoinColumn(name="listing_id")
    private JobListing jobListing;

}