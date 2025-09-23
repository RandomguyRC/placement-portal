package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    private final StudentService studentService;

    public SignupController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // signup.html
    }

    @PostMapping("/signup")
    public String registerStudent(@ModelAttribute Student student, Model model) {
        String message = studentService.register(student);
        model.addAttribute("message", message);
        return "signup"; // reload page with message
    }
}
