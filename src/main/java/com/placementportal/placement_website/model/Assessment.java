package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Assessment {

    @Id
    @Column(name = "assessment_id")
    private String assessmentId;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private JobListing jobListing;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
