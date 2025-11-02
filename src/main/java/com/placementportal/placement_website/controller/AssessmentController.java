package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.*;
import com.placementportal.placement_website.repository.*;
import com.placementportal.placement_website.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ConductedAtRepository conductedAtRepository;

    @Autowired
    private DutyAssignmentService dutyAssignmentService;

    @Autowired
    private AssessmentService assessmentService;

    // ---------------------- Existing Endpoints ----------------------

    @PostMapping("/create")
    public Assessment createAssessment(@RequestBody Assessment assessment) {
        // Save new assessment
        Assessment savedAssessment = assessmentRepository.save(assessment);

        // Fetch all venues linked with this assessment
        List<ConductedAt> venues = conductedAtRepository.findByAssessment_AssessmentId(savedAssessment.getAssessmentId());

        // Automatically assign duties to invigilators (TPRs)
        dutyAssignmentService.assignDuties(savedAssessment, venues);

        return savedAssessment;
    }

    @GetMapping
    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Assessment getAssessmentById(@PathVariable String id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
    }

    // ---------------------- Upcoming Interviews Endpoint ----------------------

    /**
     * Returns all INTERVIEW-type assessments for a given student and date.
     * Example: /api/assessments/upcoming?studentId=STU001&date=2025-11-03
     */
    @GetMapping("/upcoming")
    public List<Assessment> getUpcomingInterviews(
            @RequestParam String studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return assessmentService.getUpcomingInterviews(studentId, date);
    }
}
