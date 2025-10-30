package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.repository.JobListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyController {

    @Autowired
    private JobListingRepository jobListingRepository;

    // When user visits /companies → show company_listing.html
    @GetMapping("/companies")
    public String showCompanies(Model model) {
        model.addAttribute("jobListings", jobListingRepository.findAllWithCompany());
        return "company_listing";  // ✅ template name (company_listing.html)
    }
}
