package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.ConductedAt;
import com.placementportal.placement_website.model.ConductedAtId;
import com.placementportal.placement_website.model.Assessment;
import com.placementportal.placement_website.model.Venue;
import com.placementportal.placement_website.repository.AssessmentRepository;
import com.placementportal.placement_website.repository.VenueRepository;
import com.placementportal.placement_website.repository.ConductedAtRepository;
import com.placementportal.placement_website.service.DutyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conductedat")
public class ConductedAtController {

    @Autowired
    private ConductedAtRepository conductedAtRepo;

    @Autowired
    private AssessmentRepository assessmentRepo;

    @Autowired
    private VenueRepository venueRepo;

    @Autowired
    private DutyService dutyService;

    /**
     * Adds a record to conducted_at and automatically assigns duty.
     */
    @PostMapping("/add")
    public String addConductedAt(@RequestParam("assessmentId") String assessmentId,
                                 @RequestParam("venueId") String venueId) {

        // Step 1: Fetch Assessment & Venue
        Assessment assessment = assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        Venue venue = venueRepo.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        // Step 2: Create ConductedAtId and ConductedAt entity
        ConductedAtId conductedAtId = new ConductedAtId();
        conductedAtId.setAssessmentId(assessmentId);
        conductedAtId.setVenueId(venueId);

        ConductedAt conductedAt = new ConductedAt();
        conductedAt.setId(conductedAtId);
        conductedAt.setAssessment(assessment);
        conductedAt.setVenue(venue);

        // Step 3: Save the new record
        conductedAtRepo.save(conductedAt);

        // Step 4: Automatically assign duties
        dutyService.assignDuties(assessmentId);

        return "✅ ConductedAt record added and duty assigned successfully!";
    }
}