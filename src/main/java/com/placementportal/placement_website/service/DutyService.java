package com.placementportal.placement_website.service;

import com.placementportal.placement_website.model.*;
import com.placementportal.placement_website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DutyService {

@Autowired private DutyRepository dutyRepo;
@Autowired private TprRepository tprRepo;
@Autowired private VenueRepository venueRepo;
@Autowired private AssessmentRepository assessRepo;

/**
 * Assign multiple TPRs for an assessment.
 * Each venue gets tprCount TPRs in a round-robin fashion.
 */
@Transactional
public void assignDuties(String assessmentId) {
    Assessment assessment = assessRepo.findById(assessmentId)
            .orElseThrow(() -> new RuntimeException("Assessment not found"));

    List<Venue> venues = venueRepo.findByAssessmentId(assessmentId);
    List<Tpr> tprs = tprRepo.findAll();

    if (tprs.isEmpty()) {
        throw new RuntimeException("No TPRs available");
    }

    int tprIndex = 0;

    for (Venue venue : venues) {
        // Assign TPRs based on venue’s tprCount or default to 1
        int numAssignments = Math.max(1, venue.getTprCount());

        for (int i = 0; i < numAssignments; i++) {
            // Pick next TPR (round-robin)
            Tpr assignedTpr = tprs.get(tprIndex % tprs.size());
            tprIndex++;

            Duty duty = new Duty();
            duty.setDutyId(UUID.randomUUID().toString());
            duty.setTpr(assignedTpr);
            duty.setVenue(venue);
            duty.setRelatedListing(assessment.getJobListing());
            duty.setStatus(Duty.DutyStatus.UPCOMING);

            dutyRepo.save(duty);

            // Increment and persist TPR duty count
            Integer currentCount = assignedTpr.getDutyCount();
            assignedTpr.setDutyCount((currentCount == null ? 0 : currentCount) + 1);
            tprRepo.update(assignedTpr);
        }
    }
}

public List<Duty> getDutiesByTpr(String tprId) {
    return dutyRepo.findByTprId(tprId);
}


}
