package com.placementportal.placement_website.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class AssessmentNativeRepository {

    private final JdbcTemplate jdbcTemplate;

    public AssessmentNativeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> findSchedulesByStudent(String studentId) {
        String sql = """
            SELECT 
                a.assessment_id,
                a.type,
                a.start_time,
                a.end_time,
                c.company_name,
                j.job_role,
                j.mode_of_hiring
            FROM applications app
            JOIN job_listings j ON app.listing_id = j.listing_id
            JOIN companies c ON j.company_id = c.company_id
            JOIN assessment a ON a.listing_id = j.listing_id
            WHERE app.student_id = ?
              AND a.start_time > NOW()
            ORDER BY a.start_time ASC
        """;
        return jdbcTemplate.queryForList(sql, studentId);
    }
}
