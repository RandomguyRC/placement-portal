package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Token;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public TokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ Save new token
    public int save(Token token) {
        String sql = "INSERT INTO tokens (token_id, student_id, title, description, category, status, priority, assigned_to, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                token.getTokenId(),
                token.getStudentId(),
                token.getTitle(),
                token.getDescription(),
                token.getCategory(),
                token.getStatus(),
                token.getPriority(),
                token.getAssignedTo(),
                token.getCreatedAt(),
                token.getUpdatedAt()
        );
    }

    // ✅ Get all tokens of a student
    public List<Token> findByStudentId(String studentId) {
        String sql = "SELECT * FROM tokens WHERE student_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new TokenRowMapper(), studentId);
    }

    // ✅ Get all tokens (for TPR view)
    public List<Token> findAll() {
        String sql = "SELECT * FROM tokens ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new TokenRowMapper());
    }

    // ✅ Find token by numeric ID
    public Optional<Token> findById(Long id) {
        String sql = "SELECT * FROM tokens WHERE id = ?";
        List<Token> list = jdbcTemplate.query(sql, new TokenRowMapper(), id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    // ✅ Update token status
    public int updateStatus(Long id, String status) {
        String sql = "UPDATE tokens SET status = ?, updated_at = ? WHERE id = ?";
        return jdbcTemplate.update(sql, status, LocalDateTime.now(), id);
    }

    // ✅ Update reply, status, assigned_to, resolved_at, updated_at (TPR reply)
    public int updateReplyStatusAndAssign(Long id, String reply, String status, String assignedTo, LocalDateTime resolvedAt) {
    String sql = "UPDATE tokens SET tpr_reply = ?, status = ?, assigned_to = ?, resolved_at = ?, updated_at = ? WHERE id = ?";
    return jdbcTemplate.update(sql, reply, status, assignedTo, resolvedAt, LocalDateTime.now(), id);
}


    // ✅ Mapper
    private static class TokenRowMapper implements RowMapper<Token> {
        @Override
        public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
            Token token = new Token();
            token.setId(rs.getLong("id"));
            token.setTokenId(rs.getString("token_id"));
            token.setStudentId(rs.getString("student_id"));
            token.setTitle(rs.getString("title"));
            token.setDescription(rs.getString("description"));
            token.setCategory(rs.getString("category"));
            token.setStatus(rs.getString("status"));
            token.setPriority(rs.getString("priority"));
            token.setAssignedTo(rs.getString("assigned_to"));
            token.setTprReply(rs.getString("tpr_reply"));

            if (rs.getTimestamp("created_at") != null)
                token.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            if (rs.getTimestamp("updated_at") != null)
                token.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            if (rs.getTimestamp("resolved_at") != null)
                token.setResolvedAt(rs.getTimestamp("resolved_at").toLocalDateTime());

            return token;
        }
    }
}
