package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Attendance;
import com.placementportal.placement_website.model.AttendanceId;
import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.repository.AttendanceRepository;
import com.placementportal.placement_website.service.AttendanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Mark attendance.
     * Accepts JSON body with:
     *  - studentId (required)
     *  - dutyId OR assessmentId (dutyId preferred)
     */
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@RequestBody Map<String, String> body, HttpSession session) {
        try {
            String studentId = body.get("studentId");
            String dutyId = body.get("dutyId");
            String assessmentId = body.get("assessmentId");

            if (studentId == null || studentId.isBlank()) {
                return ResponseEntity.badRequest().body("studentId is required.");
            }

            // ✅ Get logged-in TPR
            Tpr loggedInTpr = (Tpr) session.getAttribute("tpr");
            if (loggedInTpr == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "No TPR logged in."));
            }
            String tprId = loggedInTpr.getTprId();

            // ✅ Resolve assessment_id from duty_id
            if (dutyId != null && !dutyId.isBlank()) {
                try {
                    String sql = """
                        SELECT a.assessment_id
                        FROM duties d
                        JOIN job_listings j ON d.related_listing_id = j.listing_id
                        JOIN assessment a ON a.listing_id = j.listing_id
                        WHERE d.duty_id = ?
                    """;
                    assessmentId = jdbcTemplate.queryForObject(sql, String.class, dutyId);
                } catch (EmptyResultDataAccessException ex) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("No assessment found for dutyId: " + dutyId);
                }
            }

            if (assessmentId == null || assessmentId.isBlank()) {
                return ResponseEntity.badRequest().body("Either dutyId or assessmentId must be provided and valid.");
            }

            // Optional: validate assessment exists
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(1) FROM assessment WHERE assessment_id = ?",
                    Integer.class, assessmentId);
            if (count == null || count == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Resolved assessment_id does not exist: " + assessmentId);
            }

            // Optional: validate student exists
            Integer sCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(1) FROM student_details WHERE enrollment_number = ?",
                    Integer.class, studentId);
            if (sCount == null || sCount == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Student not found: " + studentId);
            }

            // Prevent duplicate
            boolean exists = attendanceRepository.existsById(new AttendanceId(studentId, assessmentId));
            if (exists) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Attendance already recorded for student " + studentId + " and assessment " + assessmentId);
            }

            // Create and save attendance
            Attendance attendance = new Attendance();
            attendance.setStudentId(studentId);
            attendance.setAssessmentId(assessmentId);
            attendance.setIsPresent(Boolean.TRUE);
            attendance.setRecordedAt(LocalDateTime.now());
            attendance.setRecordedBy(tprId); // ✅ use logged-in TPR

            attendanceRepository.save(attendance);

            return ResponseEntity.ok(Map.of("success", true, "studentId", studentId, "assessmentId", assessmentId));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error recording attendance: " + e.getMessage());
        }
    }
    @GetMapping("/download/{assessmentId}")
public ResponseEntity<byte[]> downloadAttendanceCsv(@PathVariable String assessmentId) {
    try {
        // Fetch attendance records for the given assessment
        String sql = """
            SELECT student_id, is_present, recorded_at, recorded_by
            FROM attendances
            WHERE assessment_id = ?
            ORDER BY recorded_at ASC
        """;

        var rows = jdbcTemplate.queryForList(sql, assessmentId);

        if (rows.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No attendance records found.".getBytes());
        }

        // Build CSV content
        StringBuilder csv = new StringBuilder("student_id,is_present,recorded_at,recorded_by\n");
        for (var row : rows) {
            csv.append(row.get("student_id")).append(",")
               .append(row.get("is_present")).append(",")
               .append(row.get("recorded_at")).append(",")
               .append(row.get("recorded_by")).append("\n");
        }

        byte[] bytes = csv.toString().getBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=attendance_" + assessmentId + ".csv")
                .header("Content-Type", "text/csv")
                .body(bytes);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("Error generating CSV: " + e.getMessage()).getBytes());
    }
}

}
