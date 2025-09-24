package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.service.StudentService;
import com.placementportal.placement_website.service.TprService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    private final StudentService studentService;
    private final TprService tprService;

    public SignupController(StudentService studentService, TprService tprService) {
        this.studentService = studentService;
        this.tprService = tprService;
    }

    // ---------------- STUDENT SIGNUP ----------------
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // signup.html
    }

    @PostMapping("/signup")
    public String registerStudent(@ModelAttribute Student student,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  Model model) {

        // 1. Validate confirm password
        if (!student.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "signup";
        }

        // 2. Call service to register
        String message = studentService.register(student);

        if (message.startsWith("Error")) {
            model.addAttribute("error", message);
            return "signup";
        }

        // Success → redirect to login
        model.addAttribute("success", message);
        return "redirect:/login";
    }

    // ---------------- TPR SIGNUP ----------------
    @PostMapping("/signup/tpr")
    public String registerTpr(@ModelAttribute Tpr tpr,
                              @RequestParam("confirmPassword") String confirmPassword,
                              Model model) {

        // 1. Validate confirm password
        if (!tpr.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "signup";
        }

        // 2. Call service to register TPR
        String message = tprService.register(tpr);

        if (message.startsWith("Error")) {
            model.addAttribute("error", message);
            return "signup";
        }

        // Success → redirect to login
        model.addAttribute("success", message);
        return "redirect:/login";
    }
}
