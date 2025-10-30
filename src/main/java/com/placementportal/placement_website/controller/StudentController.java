package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ✅ Get all students (only if logged in)
    @GetMapping
    public ResponseEntity<?> getAllStudents(HttpSession session) {
        if (!isLoggedIn(session)) {
            return ResponseEntity.status(401).body("Unauthorized access. Please log in first.");
        }
        return ResponseEntity.ok(studentRepository.findAll());
    }

    // ✅ Get students by branch (only if logged in)
    @GetMapping("/branch/{branch}")
    public ResponseEntity<?> getStudentsByBranch(@PathVariable String branch, HttpSession session) {
        if (!isLoggedIn(session)) {
            return ResponseEntity.status(401).body("Unauthorized access. Please log in first.");
        }
        return ResponseEntity.ok(studentRepository.findByBranch(branch));
    }

    // ✅ Get student by enrollment number (only if logged in)
    @GetMapping("/{enrollmentNumber}")
    public ResponseEntity<?> getStudentByEnrollment(@PathVariable String enrollmentNumber, HttpSession session) {
        if (!isLoggedIn(session)) {
            return ResponseEntity.status(401).body("Unauthorized access. Please log in first.");
        }
        Student student = studentRepository.findByEnrollmentNumber(enrollmentNumber);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // ✅ Helper method to check login session
    private boolean isLoggedIn(HttpSession session) {
        Object student = session.getAttribute("student");
        Object tpr = session.getAttribute("tpr");
        return student != null || tpr != null;
    }
}
