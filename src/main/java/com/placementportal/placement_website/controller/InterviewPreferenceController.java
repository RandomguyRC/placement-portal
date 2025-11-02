package com.placementportal.placement_website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InterviewPreferenceController {

    @GetMapping("/interview-preferences")
    public String showInterviewPreferencesPage(Model model) {
        // You can later fetch upcoming interviews for the logged-in student
        // and add them to the model, e.g.:
        // model.addAttribute("interviews", interviewService.getUpcomingInterviews(studentId, LocalDate.now()));

        return "preferences"; // 👈 This corresponds to templates/preferences.html
    }
}
