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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Controller
public class CompanyController {

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private CompanyRepository companyRepository; // ✅ add this

    @Autowired
    private NotificationService notificationService; // ✅ add this

    @GetMapping("/companies")
    public String showCompanies(HttpSession session, Model model) {
        Object student = session.getAttribute("student");
        Object tpr = session.getAttribute("tpr");

        if (student == null && tpr == null) {
            return "redirect:/login";
        }

        model.addAttribute("student", student);
        model.addAttribute("tpr", tpr);

        List<JobListing> jobListings = jobListingRepository.findAll();
        model.addAttribute("jobListings", jobListings);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> eligibilityMap = new HashMap<>();

        for (JobListing job : jobListings) {
            String json = job.getEligibilityCriteria();
            StringBuilder formatted = new StringBuilder();

            if (json != null && !json.isEmpty()) {
                try {
                    JsonNode root = mapper.readTree(json);
                    if (root.has("cpi")) {
                        formatted.append("Minimum CPI: ").append(root.get("cpi").asDouble()).append("<br>");
                    }
                    if (root.has("branch")) {
                        JsonNode branches = root.get("branch");
                        if (branches.isArray() && branches.size() > 0) {
                            List<String> branchList = new ArrayList<>();
                            for (JsonNode b : branches) branchList.add(b.asText());
                            formatted.append("Allowed Branches: ")
                                     .append(String.join(", ", branchList))
                                     .append("<br>");
                        }
                    }
                    if (root.has("gender")) {
                        formatted.append("Eligible Gender: ").append(root.get("gender").asText()).append("<br>");
                    }
                } catch (Exception e) {
                    formatted.append("Eligibility data unavailable.<br>");
                }
            } else {
                formatted.append("Open to all branches and CPIs.<br>");
            }

            eligibilityMap.put(job.getListingId(), formatted.toString());
        }

        model.addAttribute("eligibilityMap", eligibilityMap);
        return "company-listing";
    }

    @PostMapping("/companies/add-job")
    public String addJobListing(@ModelAttribute JobListing jobListing, HttpSession session) {
        Object tpr = session.getAttribute("tpr");
        if (tpr == null) {
            return "redirect:/login";
        }

        jobListing.setListingId(UUID.randomUUID().toString());

        if (jobListing.getCompany() != null && jobListing.getCompany().getCompanyId() != null) {
            Company company = companyRepository.findById(jobListing.getCompany().getCompanyId()).orElse(null);
            jobListing.setCompany(company);
        }

        JobListing savedListing = jobListingRepository.save(jobListing);

        notificationService.notifyEligibleStudents(savedListing);

        return "redirect:/companies";
    }
}
