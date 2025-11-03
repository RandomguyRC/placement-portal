package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/api/preferences")
public class StudentPreferenceController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // -------------------------------
    // Utility: Ensure only student can access
    // -------------------------------
    private Student getLoggedInStudent(HttpSession session) {
        Object studentObj = session.getAttribute("student");
        if (studentObj == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }
        return (Student) studentObj;
    }

    // ✅ Get assessments for a given date
    @GetMapping
public ResponseEntity<?> getAssessmentsForDate(@RequestParam String date, HttpSession session) {
    try {
        Student student = getLoggedInStudent(session);
        String studentId = student.getEnrollmentNumber();

        // 🟢 First check if student has saved preferences for this date
        String prefCheckSQL = """
            SELECT sp.listing_id, jl.job_role AS title, c.company_name AS company,
                   a.assessment_id AS id, a.type, a.start_time, a.end_time, sp.preference_order
            FROM student_preferences sp
            JOIN job_listings jl ON sp.listing_id = jl.listing_id
            JOIN companies c ON jl.company_id = c.company_id
            JOIN assessment a ON jl.listing_id = a.listing_id
            WHERE sp.student_id = ?
              AND DATE(a.start_time) = ?
            ORDER BY sp.preference_order
        """;

        List<Map<String, Object>> savedPrefs = jdbcTemplate.queryForList(prefCheckSQL, studentId, date);

        if (!savedPrefs.isEmpty()) {
            // ✅ Student has saved preferences → return them
            return ResponseEntity.ok(savedPrefs);
        }

        // 🔹 No preferences saved yet → return by start_time
        String sql = """
            SELECT 
                a.assessment_id AS id,
                c.company_name AS company,
                jl.job_role AS title,
                a.type AS type,
                a.start_time,
                a.end_time
            FROM applications ap
            JOIN job_listings jl ON ap.listing_id = jl.listing_id
            JOIN companies c ON jl.company_id = c.company_id
            JOIN assessment a ON jl.listing_id = a.listing_id
            WHERE ap.student_id = ?
              AND ap.willingness = TRUE
              AND DATE(a.start_time) = ?
            ORDER BY a.start_time
        """;

        List<Map<String, Object>> assessments = jdbcTemplate.queryForList(sql, studentId, date);
        return ResponseEntity.ok(assessments);

    } catch (RuntimeException e) {
        return ResponseEntity.status(401).body("Access denied — only students can view preferences");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Error fetching preferences");
    }
}

    // ✅ Save student preferences for a date
    @PostMapping("/save")
    public ResponseEntity<?> savePreferences(@RequestBody Map<String, Object> payload, HttpSession session) {
        try {
            Student student = getLoggedInStudent(session);
            String studentId = student.getEnrollmentNumber();

            String date = (String) payload.get("date");
            List<String> assessmentOrder = (List<String>) payload.get("order");

            if (date == null || assessmentOrder == null || assessmentOrder.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid input data");
            }

            // ✅ Delete existing preferences for this date
            jdbcTemplate.update("""
                DELETE FROM student_preferences 
                WHERE student_id = ? 
                  AND listing_id IN (
                      SELECT jl.listing_id
                      FROM job_listings jl
                      JOIN assessment a ON jl.listing_id = a.listing_id
                      WHERE DATE(a.start_time) = ?
                  )
            """, studentId, date);

            // ✅ Insert new preferences (convert assessment_id → listing_id)
            String insertSQL = """
                INSERT INTO student_preferences (preference_id, student_id, listing_id, preference_order, created_at)
                VALUES (?, ?, 
                    (SELECT listing_id FROM assessment WHERE assessment_id = ?),
                    ?, NOW()
                )
            """;

            int order = 1;
            for (String assessmentId : assessmentOrder) {
                jdbcTemplate.update(insertSQL, UUID.randomUUID().toString(), studentId, assessmentId, order++);
            }

            return ResponseEntity.ok("Preferences saved successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Access denied — only students can save preferences");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving preferences");
        }
    }
}
