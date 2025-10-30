package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.ConductedAt;
import com.placementportal.placement_website.model.ConductedAtId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConductedAtRepository extends JpaRepository<ConductedAt, ConductedAtId> {

    // Fetch all venues for a given assessment
    List<ConductedAt> findByAssessment_AssessmentId(String assessmentId);

    // Fetch all assessments happening in a given venue
    List<ConductedAt> findByVenue_VenueId(String venueId);
}