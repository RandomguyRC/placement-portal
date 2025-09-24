package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.model.Tpr;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/homepage"})
    public String homepage(HttpSession session, Model model) {
        // Check if a student is logged in
        Student student = (Student) session.getAttribute("student");
        // Check if a TPR is logged in
        Tpr tpr = (Tpr) session.getAttribute("tpr");

        // Add both to model so Thymeleaf can use them
        model.addAttribute("student", student);
        model.addAttribute("tpr", tpr);

        return "homepage"; // Thymeleaf template
    }
}
