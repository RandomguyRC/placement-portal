package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Assessment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AssessmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AssessmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Insert new assessment
    public int save(Assessment assessment) {
        String sql = "INSERT INTO assessment (assessment_id, type, start_time, end_time, listing_id, seating_plan) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                assessment.getAssessmentId(),
                assessment.getType(),
                assessment.getStartTime(),
                assessment.getEndTime(),
                assessment.getListingId(),
                assessment.getSeatingPlan()
        );
    }

    // Get all OAs
    public List<Assessment> findAllOA() {
        String sql = "SELECT * FROM assessment WHERE type = 'OA'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToAssessment(rs));
    }

    // Get OA by ID
    public Assessment findById(String id) {
        String sql = "SELECT * FROM assessment WHERE assessment_id = ?";
        List<Assessment> result = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToAssessment(rs), id);
        return result.isEmpty() ? null : result.get(0);
    }

    // Update seating plan
    public int updateSeatingPlan(String assessmentId, String seatingPlan) {
        String sql = "UPDATE assessment SET seating_plan = ? WHERE assessment_id = ?";
        return jdbcTemplate.update(sql, seatingPlan, assessmentId);
    }

    private Assessment mapRowToAssessment(ResultSet rs) throws SQLException {
        Assessment a = new Assessment();
        a.setAssessmentId(rs.getString("assessment_id"));
        a.setType(rs.getString("type"));
        a.setStartTime(rs.getTimestamp("start_time"));
        a.setEndTime(rs.getTimestamp("end_time"));
        a.setListingId(rs.getString("listing_id"));
        a.setSeatingPlan(rs.getString("seating_plan"));
        return a;
    }
}
