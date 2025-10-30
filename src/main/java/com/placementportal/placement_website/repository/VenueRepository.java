package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, String> {

    // Custom query to fetch all venues where a particular assessment is conducted
    @Query(value = "SELECT v.* FROM venue v " +
                   "JOIN conducted_at c ON v.venue_id = c.venue_id " +
                   "WHERE c.assessment_id = :assessmentId", nativeQuery = true)
    List<Venue> findByAssessmentId(String assessmentId);
}
