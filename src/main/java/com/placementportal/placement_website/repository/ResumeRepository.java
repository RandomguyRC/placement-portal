package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Resume;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ResumeRepository {
    private final JdbcTemplate jdbcTemplate;

    public ResumeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Resume> MAPPER = new RowMapper<>() {
        @Override
        public Resume mapRow(ResultSet rs, int rowNum) throws SQLException {
            Resume r = new Resume();
            r.setResumeId(rs.getString("resume_id"));
            r.setStudentId(rs.getString("student_id"));
            r.setResumeFile(rs.getString("resume_file"));
            r.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());

            r.setStatus(rs.getString("status"));
            return r;
        }
    };

    public int save(String resumeId, String studentId, String resumeFile, LocalDateTime uploadedAt, String status) {
        String sql = "INSERT INTO resume (resume_id, student_id, resume_file, uploaded_at, status) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, resumeId, studentId, resumeFile, uploadedAt, status);
    }

    public List<Resume> findByStudentId(String studentId) {
        String sql = "SELECT * FROM resume WHERE student_id = ?";
        return jdbcTemplate.query(sql, MAPPER, studentId);
    }

    public int countByStudentId(String studentId) {
        String sql = "SELECT COUNT(*) FROM resume WHERE student_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, studentId);
    }
    public int deleteByResumeIdAndStudentId(String resumeId, String studentId) {
    String sql = "DELETE FROM resume WHERE resume_id = ? AND student_id = ?";
    return jdbcTemplate.update(sql, resumeId, studentId);
}
public int updateStatus(String resumeId, String newStatus) {
    String sql = "UPDATE resume SET status = ? WHERE resume_id = ?";
    return jdbcTemplate.update(sql, newStatus, resumeId);
}

public List<Resume> findAll() {
    String sql = "SELECT * FROM resume";
    return jdbcTemplate.query(sql, MAPPER);
}

}