package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, String> {

    /**
     * Fetches all INTERVIEW-type assessments for a given student and date.
     */
    @Query("""
           SELECT a 
           FROM Assessment a 
           JOIN a.jobListing jl 
           WHERE a.type = com.placementportal.placement_website.model.AssessmentType.INTERVIEW
             AND DATE(a.startTime) = :date
             AND jl.listingId IN (
                 SELECT sp.jobListing.listingId
                 FROM StudentPreference sp
                 WHERE sp.studentId = :studentId
             )
           """)
    List<Assessment> findByInterviewDateAndStudent(@Param("date") LocalDate date,
                                                   @Param("studentId") String studentId);


    /**
     * Fetches all upcoming INTERVIEW-type assessments (after given date).
     */
    @Query("""
           SELECT a 
           FROM Assessment a 
           JOIN a.jobListing jl 
           WHERE a.type = com.placementportal.placement_website.model.AssessmentType.INTERVIEW
             AND DATE(a.startTime) > :currentDate
             AND jl.listingId IN (
                 SELECT sp.jobListing.listingId
                 FROM StudentPreference sp
                 WHERE sp.studentId = :studentId
             )
           ORDER BY a.startTime ASC
           """)
    List<Assessment> findUpcomingInterviews(@Param("studentId") String studentId,
                                            @Param("currentDate") LocalDate currentDate);
}
