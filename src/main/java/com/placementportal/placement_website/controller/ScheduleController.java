package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
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

    @GetMapping("/student")
    public ResponseEntity<?> getStudentSchedule(HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }

        String studentId = student.getEnrollmentNumber();

        String sql = """
            SELECT 
                a.assessment_id AS id,
                c.company_name AS company,
                jl.job_role AS title,
                a.start_time,
                a.end_time,
                a.listing_id,
                a.type,
                jl.mode_of_hiring AS venue
            FROM applications ap
            JOIN assessment a ON ap.listing_id = a.listing_id
            JOIN job_listings jl ON jl.listing_id = a.listing_id
            JOIN companies c ON jl.company_id = c.company_id
            WHERE ap.student_id = ? AND ap.willingness = TRUE AND a.type = 'OA'
            ORDER BY a.start_time DESC
        """;

        List<Map<String, Object>> scheduleList = jdbcTemplate.queryForList(sql, studentId);

        return ResponseEntity.ok(scheduleList);
    }
}
