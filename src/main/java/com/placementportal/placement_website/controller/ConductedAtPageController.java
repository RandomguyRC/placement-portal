package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.ConductedAt;
import com.placementportal.placement_website.model.ConductedAtId;
import com.placementportal.placement_website.model.Assessment;
import com.placementportal.placement_website.model.Venue;
import com.placementportal.placement_website.repository.AssessmentRepository;
import com.placementportal.placement_website.repository.VenueRepository;
import com.placementportal.placement_website.repository.ConductedAtRepository;
import com.placementportal.placement_website.service.DutyService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConductedAtPageController {

    @Autowired
    private AssessmentRepository assessmentRepo;

    @Autowired
    private VenueRepository venueRepo;

    @Autowired
    private ConductedAtRepository conductedAtRepo;

    @Autowired
    private DutyService dutyService;

    // ✅ Step 1: Load the page (only for logged-in TPRs)
    @GetMapping("/conductedat")
    public String showConductedAtPage(Model model, HttpSession session) {
        Object tpr = session.getAttribute("tpr");
        Object student = session.getAttribute("student");

        // Add these to the model so Thymeleaf can detect login state
        model.addAttribute("tpr", tpr);
        model.addAttribute("student", student);

        // Restrict access
        if (tpr == null) {
            return "redirect:/login";
        }

        model.addAttribute("assessments", assessmentRepo.findAll());
        model.addAttribute("venues", venueRepo.findAll());
        model.addAttribute("conductedAtList", conductedAtRepo.findAll());
        return "conductedat";
    }

    // ✅ Step 2: Handle form submission (only TPR)
    @PostMapping("/conductedat/add-page")
    public String addConductedAt(@RequestParam("assessmentId") String assessmentId,
                                 @RequestParam("venueId") String venueId,
                                 Model model,
                                 HttpSession session) {

        Object tpr = session.getAttribute("tpr");
        Object student = session.getAttribute("student");

        model.addAttribute("tpr", tpr);
        model.addAttribute("student", student);

        if (tpr == null) {
            return "redirect:/login";
        }

        // Fetch the selected Assessment and Venue
        Assessment assessment = assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        Venue venue = venueRepo.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        // Create and save ConductedAt
        ConductedAtId id = new ConductedAtId();
        id.setAssessmentId(assessmentId);
        id.setVenueId(venueId);

        ConductedAt conductedAt = new ConductedAt();
        conductedAt.setId(id);
        conductedAt.setAssessment(assessment);
        conductedAt.setVenue(venue);

        conductedAtRepo.save(conductedAt);
        dutyService.assignDuties(assessmentId);

        // Reload data
        model.addAttribute("assessments", assessmentRepo.findAll());
        model.addAttribute("venues", venueRepo.findAll());
        model.addAttribute("conductedAtList", conductedAtRepo.findAll());

        return "conductedat";
    }
}
