package com.placementportal.placement_website.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placementportal.placement_website.dto.PlanVenueDTO;
import com.placementportal.placement_website.dto.SittingPlanDTO;
import com.placementportal.placement_website.model.*;
import com.placementportal.placement_website.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SittingPlanService {

    private final ApplicationRepository applicationRepository;
    private final AssessmentRepository assessmentRepository;
    private final ConductedAtRepository conductedAtRepository;
    private final SittingPlanRepository sittingPlanRepository;
    private final ObjectMapper objectMapper;

    public SittingPlanService(ApplicationRepository applicationRepository,
            AssessmentRepository assessmentRepository,
            ConductedAtRepository conductedAtRepository,
            SittingPlanRepository sittingPlanRepository,
            ObjectMapper objectMapper) {
        this.applicationRepository = applicationRepository;
        this.assessmentRepository = assessmentRepository;
        this.conductedAtRepository = conductedAtRepository;
        this.sittingPlanRepository = sittingPlanRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Scheduled job — runs every midnight and creates sitting plans
     * for assessments whose deadline passed 1 day ago.
     */
    @Scheduled(cron = "0 0 0 * * *") // every day at midnight
    public void generatePlansAfterDeadline() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Assessment> assessments = assessmentRepository.findAll();

        for (Assessment a : assessments) {
            if (a.getJobListing().getDeadline() != null &&
                    a.getJobListing().getDeadline()
                            .toLocalDate().isEqual(yesterday)) {

                // avoid regenerating
                if (sittingPlanRepository.findByAssessmentId(a.getAssessmentId()).isEmpty()) {
                    generateAndSavePlan(a.getAssessmentId());
                }
            }
        }
    }

    /**
     * Generate sitting plan using only venues already assigned in ConductedAt.
     */
    @Transactional
    public SittingPlanDTO generateAndSavePlan(String assessmentId) {
        System.out.println("Fetching plan for: " + assessmentId);
        Optional<SittingPlan> sp = sittingPlanRepository.findByAssessmentId(assessmentId);
        System.out.println("Found? " + sp.isPresent());

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found: " + assessmentId));

        // Fetch only willing students
        String listingId = assessment.getJobListing().getListingId();
        List<Application> apps = applicationRepository.findByListingIdAndWillingnessTrueOrderByAppliedAt(listingId);

        if (apps.isEmpty()) {
            SittingPlanDTO empty = new SittingPlanDTO();
            empty.setAssessmentId(assessmentId);
            empty.setVenues(Collections.emptyList());
            persistPlanJson(assessmentId, empty);
            return empty;
        }

        // Fetch pre-assigned venues from ConductedAt
        List<ConductedAt> conductedAtList = conductedAtRepository.findByAssessment_AssessmentId(assessmentId);
        if (conductedAtList.isEmpty()) {
            throw new RuntimeException("No venues assigned in ConductedAt for assessment " + assessmentId);
        }

        List<Venue> usableVenues = conductedAtList.stream()
                .map(ConductedAt::getVenue)
                .filter(v -> v.getNoOfRows() > 0 && v.getNoOfCols() > 0)
                .sorted(Comparator.comparingInt((Venue v) -> v.getNoOfRows() * v.getNoOfCols()).reversed())
                .collect(Collectors.toList());

        int appIndex = 0;
        List<PlanVenueDTO> planVenues = new ArrayList<>();

        for (Venue venue : usableVenues) {
            if (appIndex >= apps.size())
                break;

            int rows = venue.getNoOfRows();
            int cols = venue.getNoOfCols();
            List<List<String>> layout = new ArrayList<>(rows);
            for (int r = 0; r < rows; r++) {
                List<String> rowList = new ArrayList<>(Collections.nCopies(cols, null));
                layout.add(rowList);
            }

            Map<String, String> seatStudentMap = new LinkedHashMap<>();

            for (int r = 0; r < rows && appIndex < apps.size(); r++) {
                for (int c = 0; c < cols && appIndex < apps.size(); c++) {
                    Application app = apps.get(appIndex++);
                    String seatId = venue.getVenueId() + "-R" + (r + 1) + "C" + (c + 1);
                    app.setSeatId(seatId);
                    applicationRepository.save(app);
                    layout.get(r).set(c, seatId);
                    seatStudentMap.put(seatId, app.getStudentId());
                }
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
