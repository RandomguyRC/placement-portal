package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Tpr;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TprRepository {

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public TprRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ---------------- FIND TPR BY EMAIL ----------------
    public Optional<Tpr> findByEmail(String email) {
        String sql = "SELECT * FROM tpr WHERE email = ?";
        List<Tpr> tprs = jdbcTemplate.query(
                sql,
                new Object[]{email},
                (rs, rowNum) -> {
                    Tpr tpr = new Tpr();
                    tpr.setTprId(rs.getString("tpr_id"));
                    tpr.setTprName(rs.getString("tpr_name"));
                    tpr.setEmail(rs.getString("email"));
                    tpr.setPhoneNumber(rs.getString("phone_number"));
                    tpr.setBranch(rs.getString("branch"));
                    tpr.setLocalAddress(rs.getString("local_address"));

                    String sexStr = rs.getString("sex");
                    if (sexStr != null) tpr.setSex(Tpr.Sex.valueOf(sexStr));

                    tpr.setRollNo(rs.getString("roll_no"));

                    String roleStr = rs.getString("role");
                    if (roleStr != null) tpr.setRole(Tpr.Role.valueOf(roleStr));

                    tpr.setDutyCount(rs.getInt("duty_count"));
                    tpr.setPassword(rs.getString("password"));
                    return tpr;
                }
        );
        return tprs.isEmpty() ? Optional.empty() : Optional.of(tprs.get(0));
    }

    // ---------------- SAVE TPR ----------------
    public int save(Tpr tpr) {
        String sql = "INSERT INTO tpr " +
                "(tpr_id, tpr_name, email, phone_number, branch, local_address, sex, roll_no, role, duty_count, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                tpr.getTprId(),
                tpr.getTprName(),
                tpr.getEmail(),
                tpr.getPhoneNumber(),
                tpr.getBranch(),
                tpr.getLocalAddress(),
                tpr.getSex() != null ? tpr.getSex().name() : null,
                tpr.getRollNo(),
                tpr.getRole() != null ? tpr.getRole().name() : null,
                tpr.getDutyCount() != null ? tpr.getDutyCount() : 0,
                tpr.getPassword()
        );
    }

    // ---------------- UPDATE TPR ----------------
    public int update(Tpr tpr) {
        String sql = "UPDATE tpr SET " +
                "tpr_name = ?, email = ?, phone_number = ?, branch = ?, local_address = ?, sex = ?, roll_no = ?, role = ?, duty_count = ?, password = ? " +
                "WHERE tpr_id = ?";
        return jdbcTemplate.update(sql,
                tpr.getTprName(),
                tpr.getEmail(),
                tpr.getPhoneNumber(),
                tpr.getBranch(),
                tpr.getLocalAddress(),
                tpr.getSex() != null ? tpr.getSex().name() : null,
                tpr.getRollNo(),
                tpr.getRole() != null ? tpr.getRole().name() : null,
                tpr.getDutyCount(),
                tpr.getPassword(),
                tpr.getTprId()
        );
    }

    // ---------------- PASSWORD CHECK ----------------
    public boolean checkPassword(Tpr tpr, String rawPassword) {
        String storedHash = tpr.getPassword();
        if (storedHash == null || storedHash.isEmpty()) return false;
        return passwordEncoder.matches(rawPassword, storedHash);
    }

    // ---------------- FIND BY ID ----------------
    public Optional<Tpr> findById(String tprId) {
        String sql = "SELECT * FROM tpr WHERE tpr_id = ?";
        List<Tpr> tprs = jdbcTemplate.query(
                sql,
                new Object[]{tprId},
                (rs, rowNum) -> {
                    Tpr tpr = new Tpr();
                    tpr.setTprId(rs.getString("tpr_id"));
                    tpr.setTprName(rs.getString("tpr_name"));
                    tpr.setEmail(rs.getString("email"));
                    tpr.setPhoneNumber(rs.getString("phone_number"));
                    tpr.setBranch(rs.getString("branch"));
                    tpr.setLocalAddress(rs.getString("local_address"));

                    String sexStr = rs.getString("sex");
                    if (sexStr != null) tpr.setSex(Tpr.Sex.valueOf(sexStr));

                    tpr.setRollNo(rs.getString("roll_no"));

                    String roleStr = rs.getString("role");
                    if (roleStr != null) tpr.setRole(Tpr.Role.valueOf(roleStr));

                    tpr.setDutyCount(rs.getInt("duty_count"));
                    tpr.setPassword(rs.getString("password"));
                    return tpr;
                }
        );
        return tprs.isEmpty() ? Optional.empty() : Optional.of(tprs.get(0));
    }

    // ---------------- FIND NAME BY ID ----------------
    public String findNameById(String tprId) {
        String sql = "SELECT tpr_name FROM tpr WHERE tpr_id = ?";
        List<String> names = jdbcTemplate.query(sql, new Object[]{tprId}, (rs, rowNum) -> rs.getString("tpr_name"));
        return names.isEmpty() ? "Unknown TPR" : names.get(0);
    }

    // ---------------- FIND EMAIL BY ID ----------------
    public String findEmailById(String tprId) {
        String sql = "SELECT email FROM tpr WHERE tpr_id = ?";
        List<String> emails = jdbcTemplate.query(sql, new Object[]{tprId}, (rs, rowNum) -> rs.getString("email"));
        return emails.isEmpty() ? null : emails.get(0);
    }

    // ---------------- FIND TPR WITH LEAST DUTIES ----------------
    public Optional<Tpr> findTopByOrderByDutyCountAsc() {
        String sql = "SELECT * FROM tpr ORDER BY duty_count ASC LIMIT 1";
        try {
            Tpr tpr = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Tpr leastDuty = new Tpr();
                leastDuty.setTprId(rs.getString("tpr_id"));
                leastDuty.setTprName(rs.getString("tpr_name"));
                leastDuty.setEmail(rs.getString("email"));
                leastDuty.setPhoneNumber(rs.getString("phone_number"));
                leastDuty.setBranch(rs.getString("branch"));
                leastDuty.setLocalAddress(rs.getString("local_address"));
                leastDuty.setSex(Tpr.Sex.valueOf(rs.getString("sex")));
                leastDuty.setRollNo(rs.getString("roll_no"));
                leastDuty.setRole(Tpr.Role.valueOf(rs.getString("role")));
                leastDuty.setDutyCount(rs.getInt("duty_count"));
                leastDuty.setPassword(rs.getString("password"));
                return leastDuty;
            });
            return Optional.ofNullable(tpr);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // ---------------- FIND ALL ----------------
    public List<Tpr> findAll() {
        String sql = "SELECT * FROM tpr";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Tpr tpr = new Tpr();
            tpr.setTprId(rs.getString("tpr_id"));
            tpr.setTprName(rs.getString("tpr_name"));
            tpr.setEmail(rs.getString("email"));
            tpr.setPhoneNumber(rs.getString("phone_number"));
            tpr.setBranch(rs.getString("branch"));
            tpr.setLocalAddress(rs.getString("local_address"));

            String sexStr = rs.getString("sex");
            if (sexStr != null) tpr.setSex(Tpr.Sex.valueOf(sexStr));

            tpr.setRollNo(rs.getString("roll_no"));

            String roleStr = rs.getString("role");
            if (roleStr != null) tpr.setRole(Tpr.Role.valueOf(roleStr));

            tpr.setDutyCount(rs.getInt("duty_count"));
            tpr.setPassword(rs.getString("password"));
            return tpr;
        });
    }
}
