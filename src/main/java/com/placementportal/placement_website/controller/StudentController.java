package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*") // Allow frontend access
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/branch/{branch}")
    public List<Student> getStudentsByBranch(@PathVariable String branch) {
        return studentRepository.findByBranch(branch);
    }

    @GetMapping("/{enrollmentNumber}")
    public Student getStudentByEnrollment(@PathVariable String enrollmentNumber) {
        return studentRepository.findByEnrollmentNumber(enrollmentNumber);
    }
}
