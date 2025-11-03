package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.repository.JobListingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

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

    model.addAttribute("student", student);
    model.addAttribute("tpr", tpr);

    // 📄 Fetch job listings with company info
    List<JobListing> jobListings = jobListingRepository.findAll();
    model.addAttribute("jobListings", jobListings);

    // ✅ Parse eligibility JSON and prepare readable text
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

    // 🎯 Render company-listing.html
    return "company-listing";
}
}
