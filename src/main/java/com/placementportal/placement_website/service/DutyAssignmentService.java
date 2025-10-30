package com.placementportal.placement_website.service;

import com.placementportal.placement_website.model.*;
import com.placementportal.placement_website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class DutyAssignmentService {

    @Autowired
    private TprRepository tprRepository;

    @Autowired
    private DutyRepository dutyRepository;

    @Autowired
    private JobListingRepository jobListingRepository;

    public void assignDuties(Assessment assessment, List<ConductedAt> venues) {
        List<Tpr> allTprs = tprRepository.findAll();

        if (allTprs.isEmpty()) {
            throw new RuntimeException("No TPRs found for duty assignment");
        }

        // Sort TPRs by least duty count
        allTprs.sort(Comparator.comparingInt(Tpr::getDutyCount));

        // Each venue gets one invigilator (TPR) with least current duties
        for (int i = 0; i < venues.size(); i++) {
            Tpr selectedTpr = allTprs.get(i % allTprs.size());
            selectedTpr.setDutyCount(selectedTpr.getDutyCount() + 1);
            tprRepository.save(selectedTpr);

            Duty duty = new Duty();
            duty.setDutyId(UUID.randomUUID().toString());
            duty.setTpr(selectedTpr);
            duty.setVenue(venues.get(i).getVenue());
            duty.setRelatedListing(assessment.getJobListing());
            duty.setStatus(Duty.DutyStatus.UPCOMING);
            dutyRepository.save(duty);
        }
    }
}
