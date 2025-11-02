package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentPreference {

    @Id
    @Column(name = "preference_id", length = 255)
    private String preferenceId = UUID.randomUUID().toString();

    // ✅ Student is referenced by enrollment number (string)
    @Column(name = "student_id", length = 255, nullable = false)
    private String studentId;

    // ✅ Each preference corresponds to a specific job listing
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", referencedColumnName = "listing_id", nullable = false)
    private JobListing jobListing;

    // ✅ Preference order (1, 2, 3, ...)
    @Column(name = "preference_order", nullable = false)
    private Integer preferenceOrder;

    // ✅ Timestamp for when the record was created
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 🔹 Convenience constructor (for easy creation in service/controller)
    public StudentPreference(String studentId, JobListing jobListing, Integer preferenceOrder) {
        this.preferenceId = UUID.randomUUID().toString();
        this.studentId = studentId;
        this.jobListing = jobListing;
        this.preferenceOrder = preferenceOrder;
        this.createdAt = LocalDateTime.now();
    }
}
