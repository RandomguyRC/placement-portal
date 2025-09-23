package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/login/student")
    public String loginStudent(@RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        Student student = studentRepository.findByEmail(email);

        if(student != null && student.getPassword().equals(password)) {
            // Login successful
            model.addAttribute("student", student);
            return "student-dashboard"; // create this page
        } else {
            // Login failed
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }
}
