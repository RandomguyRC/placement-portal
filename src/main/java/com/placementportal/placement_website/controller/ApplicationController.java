package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Application;
import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.repository.ApplicationRepository;
import com.placementportal.placement_website.repository.JobListingRepository;
import com.placementportal.placement_website.service.ApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobListingRepository jobListingRepository;

    // 🔹 1. Show all available job listings (for Opportunities page)
    @GetMapping("/opportunities")
    public String showOpportunities(Model model) {
        List<JobListing> jobListings = jobListingRepository.findAll();
        model.addAttribute("jobListings", jobListings);
        return "opportunities"; // opportunities.html
    }

    // 🔹 2. Apply for a job listing
    @PostMapping("/apply")
    public String applyForJob(
            @RequestParam("listingId") String listingId,
            @RequestParam(value = "willingness", defaultValue = "true") boolean willingness,
            HttpSession session,
            Model model
    ) {
        // Retrieve student ID from session (assuming you set it during login)
        String studentId = (String) session.getAttribute("studentId");

        if (studentId == null) {
            model.addAttribute("error", "Please log in to apply for a job.");
            return "redirect:/login";
        }

        // Check if already applied
        if (applicationRepository.existsByStudentIdAndListingId(studentId, listingId)) {
            model.addAttribute("message", "You have already applied for this listing.");
            return "redirect:/applications/opportunities";
        }

        // Create new application entry
        Application app = new Application();
        app.setApplicationId(UUID.randomUUID().toString());
        app.setStudentId(studentId);
        app.setListingId(listingId);
        app.setWillingness(willingness);
        app.setAppliedAt(java.time.LocalDateTime.now());
        app.setApproved(false);
        app.setSeatId(null);

        applicationRepository.save(app);

        model.addAttribute("message", "Application submitted successfully!");
        return "redirect:/applications/opportunities";
    }

    // 🔹 3. View all applications by a student
    @GetMapping("/my-applications")
    public String viewMyApplications(HttpSession session, Model model) {
        String studentId = (String) session.getAttribute("studentId");

        if (studentId == null) {
            model.addAttribute("error", "Please log in to view your applications.");
            return "redirect:/login";
        }

        List<Application> myApplications = applicationRepository.findByStudentId(studentId);
        model.addAttribute("applications", myApplications);
        return "my_applications"; // my_applications.html
    }
}
