package com.placementportal.placement_website.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placementportal.placement_website.model.Assessment;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    // 🟢 Create OA (auto generate seating plan)
    @PostMapping("/create-oa")
    public String createOA(@RequestBody Assessment newAssessment) {
        try {
            // Ensure type is OA
            String type = "OA";
            newAssessment.setType(type);

            // 🔹 Generate seating plan from applications table
            String seatingPlanJson = generateSeatingPlan(newAssessment.getListingId());
            newAssessment.setSeatingPlan(seatingPlanJson);

            // 🔹 Insert new OA into assessment table
            String sql = "INSERT INTO assessment (assessment_id, type, start_time, end_time, listing_id, seating_plan) " +
                         "VALUES (?, ?, ?, ?, ?, CAST(? AS JSON))";
            jdbcTemplate.update(sql,
                    newAssessment.getAssessmentId(),
                    type,
                    newAssessment.getStartTime(),
                    newAssessment.getEndTime(),
                    newAssessment.getListingId(),
                    seatingPlanJson
            );

            return "✅ OA created successfully with auto-generated seating plan.";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error creating OA: " + e.getMessage();
        }
    }

    // 🟡 View seating plan
    @GetMapping("/view/{assessmentId}")
    public Map<String, Object> viewSeatingPlan(@PathVariable String assessmentId) {
        String sql = "SELECT seating_plan FROM assessment WHERE assessment_id = ?";
        try {
            String json = jdbcTemplate.queryForObject(sql, String.class, assessmentId);
            if (json == null) return Map.of("error", "Seating plan not found");
            return mapper.readValue(json, Map.class);
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }

    // ==========================================================
    // 🔧 Helper: Generate seating plan for listing_id
    // ==========================================================
    private String generateSeatingPlan(String listingId) throws Exception {
        // Step 1: Fetch all approved & willing students
        String sql = """
            SELECT s.enrollment_number, s.student_name
            FROM student_details s
            JOIN applications a ON s.enrollment_number = a.student_id
            WHERE a.listing_id = ? AND a.willingness = TRUE AND a.approved = TRUE
            ORDER BY s.enrollment_number
        """;

        List<Map<String, Object>> students = jdbcTemplate.queryForList(sql, listingId);

        if (students.isEmpty()) {
            return mapper.writeValueAsString(Map.of("message", "No eligible students found"));
        }

        // Step 2: Define labs and capacity
        List<String> labs = Arrays.asList("Lab 1", "Lab 2", "Lab 3", "Lab 4");
        int capacity = 30;
        Map<String, List<Map<String, Object>>> plan = new LinkedHashMap<>();

        for (String lab : labs) {
            plan.put(lab, new ArrayList<>());
        }

        // Step 3: Distribute students
        int labIndex = 0;
        for (Map<String, Object> student : students) {
            String lab = labs.get(labIndex);
            plan.get(lab).add(student);

            if (plan.get(lab).size() >= capacity) {
                labIndex = (labIndex + 1) % labs.size();
            }
        }

        // Step 4: Convert to JSON
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(plan);
    }
}
