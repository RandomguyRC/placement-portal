package com.placementportal.placement_website.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.model.Application;
import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.ApplicationRepository;
import com.placementportal.placement_website.repository.JobListingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/opportunities")
public class OpportunitiesController {

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public String showEligibleOpportunities(HttpSession session, Model model) {
        // retrieve logged-in student from session (your SessionController stores it under "student")
        Student student = (Student) session.getAttribute("student");

        // if not logged in, show an empty list (or redirect to login depending on your flow)
        if (student == null) {
            model.addAttribute("jobListings", Collections.emptyList());
            model.addAttribute("appliedMap", Collections.emptyMap());
            model.addAttribute("notLoggedIn", true);
            return "opportunities";
        }

        // fetch all listings with company eagerly loaded
        List<JobListing> all = jobListingRepository.findAllWithCompany();

        // filter by eligibility
        List<JobListing> eligible = all.stream()
                .filter(job -> isStudentEligibleForJob(student, job))
                .collect(Collectors.toList());

        // build applied map: listingId -> boolean (true if student already applied)
        Map<String, Boolean> appliedMap = new HashMap<>();
        for (JobListing job : eligible) {
            boolean already = applicationRepository.existsByStudentIdAndListingId(student.getEnrollmentNumber(), job.getListingId());
            appliedMap.put(job.getListingId(), already);
        }

        model.addAttribute("jobListings", eligible);
        model.addAttribute("appliedMap", appliedMap);
        model.addAttribute("student", student);
        model.addAttribute("notLoggedIn", false);

        return "opportunities";
    }

    @PostMapping("/apply/{listingId}")
    @ResponseBody
    public String applyForJob(@PathVariable("listingId") String listingId, HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return "error:not_logged_in";
        }

        String studentId = student.getEnrollmentNumber();

        // duplicate check
        boolean already = applicationRepository.existsByStudentIdAndListingId(studentId, listingId);
        if (already) {
            return "error:already_applied";
        }

        Application app = new Application();
        app.setApplicationId(UUID.randomUUID().toString());
        app.setStudentId(studentId);
        app.setListingId(listingId);
        app.setWillingness(true);
        app.setAppliedAt(LocalDateTime.now());
        app.setApproved(false);

        applicationRepository.save(app);
        return "success";
    }

    // === Helper: parse eligibilityCriteria JSON and check student fields ===
    private boolean isStudentEligibleForJob(Student student, JobListing job) {
    String json = job.getEligibilityCriteria();
    if (json == null || json.trim().isEmpty()) {
        return true; // No criteria means open for all
    }

    try {
        JsonNode root = objectMapper.readTree(json);

        // Match your actual JSON: {"branch":["APD"],"cpi":10.00}

        // Check CPI
        if (root.has("cpi")) {
            double requiredCpi = root.get("cpi").asDouble(0.0);
            Double studentCpi = student.getCpi();
            if (studentCpi == null || studentCpi < requiredCpi) {
                return false;
            }
        }

        // Check branch eligibility
        // === Branch eligibility check (exact match with multiple branches in JSON) ===
if (root.has("branch")) {
    JsonNode branchArray = root.get("branch");
    if (branchArray.isArray() && branchArray.size() > 0) {
        String studentBranch = Optional.ofNullable(student.getBranch()).orElse("").trim().toLowerCase();

        // Collect allowed branches from JSON into a lowercase set
        Set<String> allowedBranches = new HashSet<>();
        for (JsonNode b : branchArray) {
            allowedBranches.add(b.asText().trim().toLowerCase());
        }

        // Check if student's branch matches any allowed branch
        if (!allowedBranches.contains(studentBranch)) {
            return false;
        }
    }
}


        return true; // passed all checks
    } catch (Exception e) {
        System.out.println("⚠️ JSON parse failed for listing " + job.getListingId() + ": " + e.getMessage());
        return true; // show listing if malformed JSON
    }
}

}
