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

        // ✅ Retrieve both student and tpr sessions
        Student student = (Student) session.getAttribute("student");
        Object tpr = session.getAttribute("tpr");

        // 🔒 Redirect to login if neither logged in
        if (student == null && tpr == null) {
            return "redirect:/login";
        }

        // ✅ Add session info for navbar / UI
        model.addAttribute("student", student);
        model.addAttribute("tpr", tpr);

        // If TPR logged in → show all job listings (no eligibility filter)
        if (tpr != null && student == null) {
            List<JobListing> allListings = jobListingRepository.findAllWithCompany();
            model.addAttribute("jobListings", allListings);
            model.addAttribute("appliedMap", Collections.emptyMap());
            model.addAttribute("notLoggedIn", false);
            return "opportunities";
        }

        // 🎓 Student logic: filter by eligibility
        List<JobListing> all = jobListingRepository.findAllWithCompany();

        List<JobListing> eligible = all.stream()
                .filter(job -> isStudentEligibleForJob(student, job))
                .collect(Collectors.toList());

        // 🔁 Map listingId -> whether student already applied
        Map<String, Boolean> appliedMap = new HashMap<>();
        for (JobListing job : eligible) {
            boolean already = applicationRepository.existsByStudentIdAndListingId(
                    student.getEnrollmentNumber(), job.getListingId());
            appliedMap.put(job.getListingId(), already);
        }

        model.addAttribute("jobListings", eligible);
        model.addAttribute("appliedMap", appliedMap);
        model.addAttribute("notLoggedIn", false);
        Map<String, String> eligibilityMap = new HashMap<>();
for (JobListing job : eligible) {
    String json = job.getEligibilityCriteria();
    StringBuilder display = new StringBuilder();

    if (json != null && !json.trim().isEmpty()) {
        try {
            JsonNode root = objectMapper.readTree(json);

            if (root.has("cpi")) {
                display.append("Min CPI: ").append(root.get("cpi").asDouble()).append("<br>");
            }

            if (root.has("branch")) {
                JsonNode branches = root.get("branch");
                if (branches.isArray() && branches.size() > 0) {
                    List<String> branchList = new ArrayList<>();
                    for (JsonNode b : branches) {
                        branchList.add(b.asText());
                    }
                    display.append("Allowed Branches: ").append(String.join(", ", branchList)).append("<br>");
                }
            }

        } catch (Exception e) {
            display.append("Eligibility data unavailable.");
        }
    } else {
        display.append("Open to all branches and CPIs.");
    }

    eligibilityMap.put(job.getListingId(), display.toString());
}

model.addAttribute("eligibilityMap", eligibilityMap);

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

        // Check if already applied
        boolean already = applicationRepository.existsByStudentIdAndListingId(studentId, listingId);
        if (already) {
            return "error:already_applied";
        }

        // Save new application
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

    // === Helper: check eligibility based on job JSON ===
    private boolean isStudentEligibleForJob(Student student, JobListing job) {
        String json = job.getEligibilityCriteria();
        if (json == null || json.trim().isEmpty()) {
            return true; // No criteria = open to all
        }

        try {
            JsonNode root = objectMapper.readTree(json);

            // Check CPI
            if (root.has("cpi")) {
                double requiredCpi = root.get("cpi").asDouble(0.0);
                Double studentCpi = student.getCpi();
                if (studentCpi == null || studentCpi < requiredCpi) {
                    return false;
                }
            }

            // Check Branch
            if (root.has("branch")) {
                JsonNode branchArray = root.get("branch");
                if (branchArray.isArray() && branchArray.size() > 0) {
                    String studentBranch = Optional.ofNullable(student.getBranch())
                            .orElse("")
                            .trim()
                            .toLowerCase();

                    Set<String> allowedBranches = new HashSet<>();
                    for (JsonNode b : branchArray) {
                        allowedBranches.add(b.asText().trim().toLowerCase());
                    }

                    if (!allowedBranches.contains(studentBranch)) {
                        return false;
                    }
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("⚠️ JSON parse failed for listing " + job.getListingId() + ": " + e.getMessage());
            return true; // Show even if eligibility JSON is broken
        }
    }
}
