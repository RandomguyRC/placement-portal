package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    @Column(name = "assessment_id", length = 255, nullable = false)
    private String assessmentId;

    // persist enum as STRING to match ENUM('INTERVIEW','OA') in DB
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AssessmentType type;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    // Many assessments belong to a job listing
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", referencedColumnName = "listing_id")
    private JobListing jobListing;

    // convenience constructor (without jobListing to avoid circular creation problems)
    public Assessment(String assessmentId, AssessmentType type, LocalDateTime startTime, LocalDateTime endTime) {
        this.assessmentId = assessmentId;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
