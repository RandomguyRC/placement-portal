package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.repository.CompanyRepository;
import com.placementportal.placement_website.repository.JobListingRepository;
import com.placementportal.placement_website.repository.StudentRepository;
import com.placementportal.placement_website.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/job-listings")
public class AdminJobListingController {

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationService notificationService;

    // View all job listings
    @GetMapping
    public String listJobListings(Model model) {
        List<JobListing> listings = jobListingRepository.findAll();
        model.addAttribute("jobListings", listings);
        return "admin/job-listings-list";
    }

    // Show Add Listing form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("jobListing", new JobListing());
        model.addAttribute("companies", companyRepository.findAll());

        List<String> branches = studentRepository.findAll().stream()
                .map(s -> s.getBranch())
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        model.addAttribute("branches", branches);

        return "admin/job-listings-add";
    }

    // Handle Add form submission
    @PostMapping("/add")
    public String addJobListing(@ModelAttribute JobListing jobListing) {
        // Ensure a unique listing ID
        if (jobListing.getListingId() == null || jobListing.getListingId().isEmpty()) {
            jobListing.setListingId(UUID.randomUUID().toString());
        }

        // Save the job listing
        JobListing savedJob = jobListingRepository.save(jobListing);

        // ✅ Trigger notifications for eligible students
        try {
            notificationService.notifyEligibleStudents(savedJob);
            System.out.println("✅ Notifications created for job listing: " + savedJob.getListingId());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Notification service failed for job listing: " + savedJob.getListingId());
        }

        return "redirect:/admin/job-listings";
    }

    // Show Edit Listing form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        JobListing listing = jobListingRepository.findById(id).orElse(null);
        model.addAttribute("jobListing", listing);
        model.addAttribute("companies", companyRepository.findAll());
        return "admin/job-listings-edit";
    }

    // Handle Update form submission
    @PostMapping("/update")
    public String updateJobListing(@ModelAttribute JobListing jobListing) {
        jobListingRepository.save(jobListing);
        return "redirect:/admin/job-listings";
    }

    // Delete listing
    @GetMapping("/delete/{id}")
    public String deleteJobListing(@PathVariable("id") String id) {
        jobListingRepository.deleteById(id);
        return "redirect:/admin/job-listings";
    }
}
