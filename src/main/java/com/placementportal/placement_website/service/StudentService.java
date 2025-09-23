package com.placementportal.placement_website.service;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.StudentRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public String register(Student student) {
        if (studentRepository.emailExists(student.getEmail())) {
            return "Email already registered!";
        }

        // 🔐 Hash password before saving
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        int result = studentRepository.save(student);
        return result > 0 ? "Signup successful!" : "Signup failed!";
    }
}
