package com.placementportal.placement_website.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placementportal.placement_website.dto.PlanVenueDTO;
import com.placementportal.placement_website.dto.SittingPlanDTO;
import com.placementportal.placement_website.model.*;
import com.placementportal.placement_website.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SittingPlanService {

    private final ApplicationRepository applicationRepository;
    private final AssessmentRepository assessmentRepository;
    private final ConductedAtRepository conductedAtRepository;
    private final VenueRepository venueRepository;
    private final SittingPlanRepository sittingPlanRepository;
    private final ObjectMapper objectMapper;

    public SittingPlanService(ApplicationRepository applicationRepository,
                              AssessmentRepository assessmentRepository,
                              ConductedAtRepository conductedAtRepository,
                              VenueRepository venueRepository,
                              SittingPlanRepository sittingPlanRepository,
                              ObjectMapper objectMapper) {
        this.applicationRepository = applicationRepository;
        this.assessmentRepository = assessmentRepository;
        this.conductedAtRepository = conductedAtRepository;
        this.venueRepository = venueRepository;
        this.sittingPlanRepository = sittingPlanRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Generate sitting plan for an assessment, persist seat ids into applications,
     * create ConductedAt entries for used venues (if not already present),
     * and save a JSON sitting_plan record.
     *
     * @param assessmentId assessment id
     * @return SittingPlanDTO JSON-serializable plan
     */
    @Transactional
    public SittingPlanDTO generateAndSavePlan(String assessmentId) {
        // 1. fetch assessment
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found: " + assessmentId));

        // 2. fetch applications for that listing (willing students first)
        String listingId = assessment.getJobListing().getListingId();
        List<Application> apps = applicationRepository.findByListingIdAndWillingnessTrueOrderByAppliedAt(listingId);
        if (apps == null || apps.isEmpty()) {
            // nothing to allocate
            SittingPlanDTO empty = new SittingPlanDTO();
            empty.setAssessmentId(assessmentId);
            empty.setVenues(Collections.emptyList());
            // persist empty plan (optional)
            persistPlanJson(assessmentId, empty);
            return empty;
        }

        // 3. fetch all venues and sort them by capacity descending (use bigger rooms first)
        List<Venue> allVenues = venueRepository.findAll();
        // filter out venues with zero capacity
        List<Venue> usableVenues = allVenues.stream()
                .filter(v -> v.getNoOfRows() > 0 && v.getNoOfCols() > 0)
                .sorted(Comparator.comparingInt((Venue v) -> v.getNoOfRows() * v.getNoOfCols()).reversed())
                .collect(Collectors.toList());

        if (usableVenues.isEmpty()) {
            // no venues in DB — create empty plan and persist
            SittingPlanDTO empty = new SittingPlanDTO();
            empty.setAssessmentId(assessmentId);
            empty.setVenues(Collections.emptyList());
            persistPlanJson(assessmentId, empty);
            return empty;
        }

        // 4. allocate students across venues sequentially
        int appIndex = 0;
        List<PlanVenueDTO> planVenues = new ArrayList<>();

        for (Venue venue : usableVenues) {
            if (appIndex >= apps.size()) break;

            int rows = venue.getNoOfRows();
            int cols = venue.getNoOfCols();
            int capacity = rows * cols;

            // init layout & seat->student map
            List<List<String>> layout = new ArrayList<>(rows);
            for (int r = 0; r < rows; r++) {
                List<String> rowList = new ArrayList<>(cols);
                for (int c = 0; c < cols; c++) rowList.add(null);
                layout.add(rowList);
            }
            Map<String, String> seatStudentMap = new LinkedHashMap<>();

            // fill seat by seat row-major
            int seatNumber = 1;
            for (int r = 0; r < rows && appIndex < apps.size(); r++) {
                for (int c = 0; c < cols && appIndex < apps.size(); c++) {
                    Application app = apps.get(appIndex++);
                    // seat id format: <venueId>-S<sequential>  OR <venueId>-R{r+1}C{c+1}
                    String seatId = venue.getVenueId() + "-R" + (r + 1) + "C" + (c + 1);

                    // update application with seatId
                    app.setSeatId(seatId);
                    applicationRepository.save(app);

                    // place in layout and map
                    layout.get(r).set(c, seatId);
                    // map seat->studentId (you have only studentId string in applications)
                    seatStudentMap.put(seatId, app.getStudentId());

                    seatNumber++;
                }
            }

            // ensure ConductedAt entry for this assessment-venue exists (so DB knows this venue is used)
            boolean exists = conductedAtRepository.existsByAssessment_AssessmentIdAndVenue_VenueId(assessmentId, venue.getVenueId());
            if (!exists) {
                // create new ConductedAt mapping
                ConductedAt ca = new ConductedAt();
                // assuming ConductedAt has setters for assessment and venue
                ca.setAssessment(assessment);
                ca.setVenue(venue);
                // set composite id or primary key handled by entity
                conductedAtRepository.save(ca);
            }

            PlanVenueDTO pv = new PlanVenueDTO();
            pv.setVenueId(venue.getVenueId());
            pv.setVenueLocation(venue.getVenueLocation());
            pv.setRows(rows);
            pv.setCols(cols);
            pv.setLayout(layout);
            pv.setSeatStudentMap(seatStudentMap);

            planVenues.add(pv);
        }

        SittingPlanDTO plan = new SittingPlanDTO();
        plan.setAssessmentId(assessmentId);
        plan.setVenues(planVenues);

        // 5. persist the plan json into sitting_plan table
        persistPlanJson(assessmentId, plan);

        return plan;
    }

    private void persistPlanJson(String assessmentId, SittingPlanDTO plan) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(plan);

            Optional<SittingPlan> existing = sittingPlanRepository.findByAssessmentId(assessmentId);
            String planId = existing.map(SittingPlan::getPlanId).orElse(UUID.randomUUID().toString());
            SittingPlan sp = new SittingPlan(planId, assessmentId, json, LocalDateTime.now());
            sittingPlanRepository.save(sp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist sitting plan JSON: " + e.getMessage(), e);
        }
    }
}
