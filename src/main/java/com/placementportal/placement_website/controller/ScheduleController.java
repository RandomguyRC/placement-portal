package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.model.Tpr;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // -------------------------------
    // Student schedule (assessments)
    // -------------------------------
    @GetMapping("/student")
    public ResponseEntity<?> getStudentSchedule(HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }

        String studentId = student.getEnrollmentNumber();

        // ✅ Fetch assessments for job listings that the student has applied to
        String sql = """
            SELECT 
                a.assessment_id AS id,
                c.company_name AS company,
                jl.job_role AS title,
                a.start_time,
                a.end_time,
                jl.mode_of_hiring AS venue,
                a.type
            FROM applications ap
            JOIN job_listings jl ON ap.listing_id = jl.listing_id
            JOIN companies c ON jl.company_id = c.company_id
            JOIN assessment a ON jl.listing_id = a.listing_id
            WHERE ap.student_id = ?
              AND ap.willingness = TRUE
            ORDER BY a.start_time DESC
        """;

        List<Map<String, Object>> scheduleList = jdbcTemplate.queryForList(sql, studentId);
        return ResponseEntity.ok(scheduleList);
    }

    // -------------------------------
    // TPR schedule (all assessments)
    // -------------------------------
    @GetMapping("/tpr")
    public ResponseEntity<?> getTprSchedule(HttpSession session) {
        Tpr tpr = (Tpr) session.getAttribute("tpr");
        if (tpr == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }

        // ✅ Fetch all assessments of all job listings
        String sql = """
            SELECT 
                a.assessment_id AS id,
                c.company_name AS company,
                jl.job_role AS title,
                a.start_time,
                a.end_time,
                jl.mode_of_hiring AS venue,
                a.type
            FROM assessment a
            JOIN job_listings jl ON a.listing_id = jl.listing_id
            JOIN companies c ON jl.company_id = c.company_id
            ORDER BY a.start_time DESC
        """;

        List<Map<String, Object>> scheduleList = jdbcTemplate.queryForList(sql);
        return ResponseEntity.ok(scheduleList);
    }

    // -------------------------------
    // Auto-detect endpoint (optional)
    // -------------------------------
    @GetMapping
    public ResponseEntity<?> getSchedule(HttpSession session) {
        if (session.getAttribute("student") != null) {
            return getStudentSchedule(session);
        } else if (session.getAttribute("tpr") != null) {
            return getTprSchedule(session);
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
}
