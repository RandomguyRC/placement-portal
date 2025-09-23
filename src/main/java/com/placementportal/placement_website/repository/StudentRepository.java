package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Student;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Student student) {
        String sql = "INSERT INTO student_details " +
                "(enrollment_number, student_name, email, phone_number, branch, address, local_address, sex, cpi, profile_stat, dob, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                student.getEnrollmentNumber(),
                student.getStudentName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getBranch(),
                student.getAddress(),
                student.getLocalAddress(),
                student.getSex(),
                student.getCpi(),
                "unverified",  // default status
                student.getDob(),  // dob as String
                student.getPassword()
        );
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM student_details WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public Student findByEmail(String email) {
        String sql = "SELECT * FROM student_details WHERE email = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{email}, // pass parameters as Object array
                new RowMapper<Student>() {
                    @Override
                    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Student student = new Student();
                        student.setEnrollmentNumber(rs.getString("enrollment_number"));
                        student.setStudentName(rs.getString("student_name"));
                        student.setEmail(rs.getString("email"));
                        student.setPhoneNumber(rs.getString("phone_number"));
                        student.setBranch(rs.getString("branch"));
                        student.setAddress(rs.getString("address"));
                        student.setLocalAddress(rs.getString("local_address"));
                        student.setSex(rs.getString("sex"));
                        student.setCpi(rs.getDouble("cpi"));

                        // Convert timestamp to formatted string
                        if(rs.getTimestamp("dob") != null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            student.setDob(rs.getTimestamp("dob").toLocalDateTime().format(formatter));
                        } else {
                            student.setDob(null);
                        }

                        student.setPassword(rs.getString("password"));
                        return student;
                    }
                }
        );
    }
}
