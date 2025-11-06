package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Student;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

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

    public int update(Student student) {
    String sql = "UPDATE student_details SET " +
            "student_name = ?, " +
            "email = ?, " +
            "phone_number = ?, " +
            "address = ?, " +
            "local_address = ?, " +
            "sex = ?, " +
            "cpi = ?, " +
            "dob = ? " +
            "WHERE enrollment_number = ?";  // enrollment_number is immutable

    return jdbcTemplate.update(sql,
            student.getStudentName(),
            student.getEmail(),
            student.getPhoneNumber(),
            student.getAddress(),
            student.getLocalAddress(),
            student.getSex(),
            student.getCpi(),
            student.getDob(),        // make sure this is in correct SQL format
            student.getEnrollmentNumber()
    );
}


    public Student findByEmail(String email) {
    String sql = "SELECT * FROM student_details WHERE email = ?";
    List<Student> students = jdbcTemplate.query(
        sql,
        new Object[]{email},
        (rs, rowNum) -> {
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

            // ✅ add this line
            student.setProfileStat(rs.getString("profile_stat"));

            if (rs.getTimestamp("dob") != null) {
                student.setDob(rs.getTimestamp("dob").toLocalDateTime().toLocalDate().toString());
            }
            student.setPassword(rs.getString("password"));
            return student;
        }
    );

    return students.isEmpty() ? null : students.get(0);
}


public List<Student> findAll() {
    String sql = "SELECT * FROM student_details";
    return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToStudent(rs));
}

public List<Student> findByBranch(String branch) {
    String sql = "SELECT * FROM student_details WHERE branch = ?";
    return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToStudent(rs), branch);
}

public Student findByEnrollmentNumber(String enrollmentNumber) {
    String sql = "SELECT * FROM student_details WHERE enrollment_number = ?";
    List<Student> students = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToStudent(rs), enrollmentNumber);
    return students.isEmpty() ? null : students.get(0);
}

// Helper function to avoid repeating mapping logic
private Student mapRowToStudent(ResultSet rs) throws SQLException {
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
    student.setProfileStat(rs.getString("profile_stat"));
    if (rs.getTimestamp("dob") != null) {
        student.setDob(rs.getTimestamp("dob").toLocalDateTime().toLocalDate().toString());
    }
    student.setPassword(rs.getString("password"));
    return student;
}

public int updateProfileStatus(String enrollmentNumber, String profileStat) {
    String sql = "UPDATE student_details SET profile_stat = ? WHERE enrollment_number = ?";
    return jdbcTemplate.update(sql, profileStat, enrollmentNumber);
}

    public List<String> findDistinctBranches() {
    String sql = "SELECT DISTINCT branch FROM student_details WHERE branch IS NOT NULL AND branch != ''";
    return jdbcTemplate.queryForList(sql, String.class);
}

}
