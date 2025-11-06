package com.placementportal.placement_website.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.model.Student;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class EligibilityService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean isStudentEligibleForJob(Student student, JobListing job) {
        String json = job.getEligibilityCriteria();

        if (json == null || json.trim().isEmpty()) {
            return true; // No restrictions
        }

        try {
            JsonNode root = objectMapper.readTree(json);

            // ✅ Check CPI
            if (root.has("cpi")) {
                double requiredCpi = root.get("cpi").asDouble(0.0);
                if (student.getCpi() == null || student.getCpi() < requiredCpi) {
                    return false;
                }
            }

            // ✅ Check Branch
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
            System.out.println("⚠️ Eligibility check failed for listing " + job.getListingId() + ": " + e.getMessage());
            return true; // fail-safe: show job if JSON malformed
        }
    }
}
