package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Company;
import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.repository.CompanyRepository;
import com.placementportal.placement_website.repository.JobListingRepository;
import com.placementportal.placement_website.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class CompanyController {

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private NotificationService notificationService;

    // ✅ Existing method — unchanged
    @GetMapping("/companies")
    public String showCompanies(HttpSession session, Model model) {
        Object student = session.getAttribute("student");
        Object tpr = session.getAttribute("tpr");

        // 🔒 Redirect to login if not logged in
        if (student == null && tpr == null) {
            return "redirect:/login";
        }

        // ✅ Add logged-in user info to model
        model.addAttribute("student", student);
        model.addAttribute("tpr", tpr);

        // 📄 Fetch job listings with company info
        List<JobListing> jobListings = jobListingRepository.findAll();
        model.addAttribute("jobListings", jobListings);

        // 🎯 Render company-listing.html (keep your existing file name)
        return "company-listing";
    }

    // ✅ New method — Add a job listing and send notifications
    @PostMapping("/companies/add-job")
    public String addJobListing(@ModelAttribute JobListing jobListing, HttpSession session) {
        Object tpr = session.getAttribute("tpr");
        if (tpr == null) {
            return "redirect:/login";
        }

        // 🔹 Generate a unique listing ID
        jobListing.setListingId(UUID.randomUUID().toString());

        // 🔹 Set company reference (assuming companyId is passed in the form)
        if (jobListing.getCompany() != null && jobListing.getCompany().getCompanyId() != null) {
            Company company = companyRepository.findById(jobListing.getCompany().getCompanyId()).orElse(null);
            jobListing.setCompany(company);
        }

        // 💾 Save job listing
        JobListing savedListing = jobListingRepository.save(jobListing);

        // 🛎️ Send notifications to eligible students
        notificationService.notifyEligibleStudents(savedListing);

        // 🔁 Redirect to company listing page
        return "redirect:/companies";
    }
}
