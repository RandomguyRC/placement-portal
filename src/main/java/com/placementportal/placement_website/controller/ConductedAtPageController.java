package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.ConductedAt;
import com.placementportal.placement_website.model.ConductedAtId;
import com.placementportal.placement_website.model.Assessment;
import com.placementportal.placement_website.model.Venue;
import com.placementportal.placement_website.repository.AssessmentRepository;
import com.placementportal.placement_website.repository.VenueRepository;
import com.placementportal.placement_website.repository.ConductedAtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.placementportal.placement_website.service.DutyService;


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

    // ✅ Step 1: Load the page with dropdowns and list
    @GetMapping("/conductedat")
    public String showConductedAtPage(Model model) {
    model.addAttribute("assessments", assessmentRepo.findAll());
    model.addAttribute("venues", venueRepo.findAll());
    model.addAttribute("conductedAtList", conductedAtRepo.findAll());
    return "conductedat";
}


    // ✅ Step 2: Handle the form submission
    @PostMapping("/conductedat/add-page")
    public String addConductedAt(@RequestParam("assessmentId") String assessmentId,
                                 @RequestParam("venueId") String venueId,
                                 Model model) {

        // Fetch the selected Assessment and Venue
        Assessment assessment = assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        Venue venue = venueRepo.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        // Create the ConductedAt object
        ConductedAtId id = new ConductedAtId();
        id.setAssessmentId(assessmentId);
        id.setVenueId(venueId);

        ConductedAt conductedAt = new ConductedAt();
        conductedAt.setId(id);
        conductedAt.setAssessment(assessment);
        conductedAt.setVenue(venue);

        // Save to DB
        conductedAtRepo.save(conductedAt);


        dutyService.assignDuties(assessmentId);

        // Reload page data
        model.addAttribute("assessments", assessmentRepo.findAll());
        model.addAttribute("venues", venueRepo.findAll());
        model.addAttribute("conductedAtList", conductedAtRepo.findAll());

        // Return same page (so user sees update)
        return "conductedat";
    }
}
