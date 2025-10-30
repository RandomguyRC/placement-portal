package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.model.Resume;
import com.placementportal.placement_website.model.Application;
import com.placementportal.placement_website.service.JobListingService;
import com.placementportal.placement_website.service.ApplicationService;
import com.placementportal.placement_website.repository.ResumeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import com.placementportal.placement_website.model.ResumeStatus;
import java.util.List;
import java.util.UUID;

@Controller
public class OpportunitiesController {

    @Autowired
    private JobListingService jobListingService;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ApplicationService applicationService;

    // ✅ Display all job opportunities
    @GetMapping("/opportunities")
    public String viewOpportunities(Model model) {
        List<JobListing> jobListings = jobListingService.getAllJobListings();
        model.addAttribute("jobListings", jobListings);
        return "opportunities";
    }

    // ✅ Handle the willingness + resume submission form
    @PostMapping("/apply")
    public String submitWillingness(
            @RequestParam("studentId") String studentId,
            @RequestParam("listingId") String listingId,
            @RequestParam("willingness") boolean willingness,
            @RequestParam("resumeFile") String resumeFile,
            Model model) {

        // 📝 Save resume info
        Resume resume = new Resume();
        resume.setResumeId(UUID.randomUUID().toString());
        resume.setStudentId(studentId);
        resume.setResumeFile(resumeFile);
        resume.setUploadedAt(LocalDateTime.now());
        resume.setStatus(ResumeStatus.UNVERIFIED);
        resumeRepository.save(resume);

        // 📝 Save application
        Application application = new Application();
        application.setApplicationId(UUID.randomUUID().toString());
        application.setStudentId(studentId);
        application.setListingId(listingId);
        application.setWillingness(willingness);
        // application.setAppliedAt(LocalDateTime.now());
        application.setAppliedAt(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        application.setApproved(false);
        // application.setResumeLink(resumeFile); // optional if Application has resumeLink

        applicationService.saveApplication(application);

        // ✅ Return to opportunities page with message
        model.addAttribute("message", "Your application has been submitted successfully!");
        model.addAttribute("jobListings", jobListingService.getAllJobListings());
        return "opportunities";
    }
}
