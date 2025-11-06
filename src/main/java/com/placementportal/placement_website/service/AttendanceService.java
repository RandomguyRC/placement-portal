package com.placementportal.placement_website.service;

import com.placementportal.placement_website.model.Attendance;
import com.placementportal.placement_website.model.AttendanceId;
import com.placementportal.placement_website.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Marks attendance using dutyId (recommended). Resolves assessment_id internally.
     *
     * @param dutyId    duty identifier from duties.duty_id
     * @param studentId student's enrollment number
     * @param recordedBy tpr id who recorded attendance
     * @throws IllegalArgumentException when inputs are invalid or required rows missing
     * @throws IllegalStateException when attendance already marked
     */
    @Transactional
    public void markAttendanceByDuty(String dutyId, String studentId, String recordedBy) {
        // 1) Validate duty exists and get related_listing_id
        String listingId;
        try {
            listingId = jdbcTemplate.queryForObject(
                    "SELECT related_listing_id FROM duties WHERE duty_id = ?",
                    String.class, dutyId);
        } catch (DataAccessException dae) {
            throw new IllegalArgumentException("Duty not found: " + dutyId);
        }

        if (listingId == null) {
            throw new IllegalArgumentException("Duty has no related listing: " + dutyId);
        }

        // 2) Resolve assessment_id for this listing (one-to-one mapping assumed)
        String assessmentId;
        try {
            assessmentId = jdbcTemplate.queryForObject(
                    "SELECT assessment_id FROM assessment WHERE listing_id = ?",
                    String.class, listingId);
        } catch (DataAccessException dae) {
            throw new IllegalArgumentException("No assessment found for listing: " + listingId);
        }
        if (assessmentId == null) {
            throw new IllegalArgumentException("No assessment found for listing: " + listingId);
        }

        // 3) Validate student exists
        Boolean studentExists = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM student_details WHERE enrollment_number = ?)",
                Boolean.class, studentId);
        if (studentExists == null || !studentExists) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }

        // 4) Optionally validate recordedBy TPR exists (best practice)
        if (recordedBy != null && !recordedBy.isBlank()) {
            Boolean tprExists = jdbcTemplate.queryForObject(
                    "SELECT EXISTS(SELECT 1 FROM tpr WHERE tpr_id = ?)",
                    Boolean.class, recordedBy);
            if (tprExists == null || !tprExists) {
                throw new IllegalArgumentException("TPR (recordedBy) not found: " + recordedBy);
            }
        }

        // 5) Prevent duplicate attendance
        AttendanceId id = new AttendanceId(studentId, assessmentId);
        if (attendanceRepository.existsById(id)) {
            throw new IllegalStateException("Attendance already marked for student " + studentId + " and assessment " + assessmentId);
        }

        // 6) Insert attendance
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setAssessmentId(assessmentId);
        attendance.setIsPresent(true);
        attendance.setRecordedAt(LocalDateTime.now());
        attendance.setRecordedBy(recordedBy);

        attendanceRepository.save(attendance);
    }

    /**
     * Alternative: mark attendance by assessmentId directly (keeps compatibility).
     */
    @Transactional
    public void markAttendanceByAssessment(String assessmentId, String studentId, String recordedBy) {
        // Validate assessment exists
        Boolean assessmentExists = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM assessment WHERE assessment_id = ?)",
                Boolean.class, assessmentId);
        if (assessmentExists == null || !assessmentExists) {
            throw new IllegalArgumentException("Assessment not found: " + assessmentId);
        }

        // Validate student exists
        Boolean studentExists = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM student_details WHERE enrollment_number = ?)",
                Boolean.class, studentId);
        if (studentExists == null || !studentExists) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }

        AttendanceId id = new AttendanceId(studentId, assessmentId);
        if (attendanceRepository.existsById(id)) {
            throw new IllegalStateException("Attendance already marked for student " + studentId + " and assessment " + assessmentId);
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setAssessmentId(assessmentId);
        attendance.setIsPresent(true);
        attendance.setRecordedAt(LocalDateTime.now());
        attendance.setRecordedBy(recordedBy);
        attendanceRepository.save(attendance);
    }
}
