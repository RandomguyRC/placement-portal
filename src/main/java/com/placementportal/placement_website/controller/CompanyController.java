package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.repository.JobListingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CompanyController {

    @Autowired
    private JobListingRepository jobListingRepository;

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
}
