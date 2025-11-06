package com.placementportal.placement_website.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.model.Notification;
import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.NotificationRepository;
import com.placementportal.placement_website.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StudentRepository studentRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Notify all eligible students when a new job listing is posted
     */
    public void notifyEligibleStudents(JobListing jobListing) {
        System.out.println("🟢 [DEBUG] Starting notification for job: " 
                + jobListing.getJobRole() + " (" + jobListing.getListingId() + ")");

        List<Student> eligibleStudents = getEligibleStudents(jobListing);
        System.out.println("👥 [DEBUG] Found " + eligibleStudents.size() + " eligible students");

        for (Student student : eligibleStudents) {
            try {
                Notification notification = new Notification(
                        UUID.randomUUID().toString(),
                        student,
                        "🎯 New job opportunity: " + jobListing.getJobRole() +
                                " at " + jobListing.getCompany().getCompanyName() +
                                ". Apply before " + jobListing.getDeadline() + "."
                );
                notificationRepository.save(notification);
            } catch (Exception e) {
                System.out.println("⚠️ [ERROR] Failed to create notification for " + 
                        student.getEnrollmentNumber() + ": " + e.getMessage());
            }
        }

        System.out.println("✅ [SUCCESS] Notifications created: " + eligibleStudents.size());
    }

    /**
     * Filter students based on eligibility criteria JSON
     */
    private List<Student> getEligibleStudents(JobListing job) {
        List<Student> allStudents = studentRepository.findAll().stream()
                .filter(s -> "verified".equalsIgnoreCase(s.getProfileStat()))
                .collect(Collectors.toList());

        String json = job.getEligibilityCriteria();
        if (json == null || json.trim().isEmpty()) {
            System.out.println("⚠️ [DEBUG] No eligibility JSON found — returning all verified students");
            return allStudents;
        }

        try {
            JsonNode root = objectMapper.readTree(json);

            double requiredCpi = root.has("min_cpi") ? root.get("min_cpi").asDouble(0.0) : 0.0;
            Set<String> allowedBranches = new HashSet<>();

            if (root.has("branches") && root.get("branches").isArray()) {
                for (JsonNode b : root.get("branches")) {
                    allowedBranches.add(b.asText().trim().toLowerCase());
                }
            }

            System.out.println("📊 [DEBUG] Min CPI: " + requiredCpi + ", Branches: " + allowedBranches);

            return allStudents.stream()
                    .filter(s -> (s.getCpi() != null && s.getCpi() >= requiredCpi))
                    .filter(s -> allowedBranches.isEmpty() ||
                            allowedBranches.contains(s.getBranch().toLowerCase()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("⚠️ [ERROR] Failed to parse eligibility JSON for " 
                    + job.getListingId() + ": " + e.getMessage());
            return allStudents;
        }
    }

    /**
     * Notify students for upcoming assessment (future use)
     */
    public void notifyAssessmentScheduled(Object assessment, JobListing jobListing) {
        List<Student> eligibleStudents = getEligibleStudents(jobListing);
        for (Student s : eligibleStudents) {
            Notification notification = new Notification(
                    UUID.randomUUID().toString(),
                    s,
                    "📝 Upcoming assessment for " + jobListing.getJobRole()
            );
            notificationRepository.save(notification);
        }
    }

    /**
     * Send reminder for approaching deadlines (future use)
     */
    public void notifyDeadlineApproaching(JobListing jobListing) {
        List<Student> appliedStudents = getEligibleStudents(jobListing);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = jobListing.getDeadline();

        if (deadline != null && deadline.isAfter(now) && deadline.minusDays(2).isBefore(now)) {
            for (Student s : appliedStudents) {
                Notification notification = new Notification(
                        UUID.randomUUID().toString(),
                        s,
                        "⏰ Reminder: Deadline approaching for " + jobListing.getJobRole() +
                                " — apply before " + jobListing.getDeadline() + "."
                );
                notificationRepository.save(notification);
            }
        }
    }
}
