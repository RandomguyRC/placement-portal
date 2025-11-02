package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.*;
import com.placementportal.placement_website.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/preferences")
public class StudentPreferenceController {

    @Autowired
    private AssessmentRepository assessmentRepo;

    @Autowired
    private StudentPreferenceRepository preferenceRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private JobListingRepository jobListingRepository;

    // Show preferences page
    @GetMapping
    public String showPreferencesPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("studentName", principal.getName());
        return "preferences"; // Thymeleaf template
    }

    // Fetch interviews for a specific date
    @GetMapping("/date")
    @ResponseBody
    public List<Assessment> getInterviewsByDate(@RequestParam("date") String date, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("User not logged in.");
        }

        Student student = studentRepo.findByEmail(principal.getName());
        if (student == null) {
            throw new RuntimeException("Student not found for email: " + principal.getName());
        }

        LocalDate localDate = LocalDate.parse(date);
        return assessmentRepo.findUpcomingInterviews(student.getEnrollmentNumber(), localDate);
    }

    // Save student preferences
    @PostMapping("/save")
    @ResponseBody
    public String savePreferences(@RequestBody Map<String, List<Map<String, Object>>> data, Principal principal) {
        if (principal == null) {
            return "User not logged in!";
        }

        Student student = studentRepo.findByEmail(principal.getName());
        if (student == null) {
            return "Student not found!";
        }

        data.forEach((slot, companies) -> {
            for (Map<String, Object> c : companies) {
                String companyName = (String) c.get("name");
                int priority = (int) c.get("priority");

                List<JobListing> jobListings = jobListingRepository.findByCompany_CompanyName(companyName);

                if (!jobListings.isEmpty()) {
                    JobListing jobListing = jobListings.get(0);

                    StudentPreference pref = new StudentPreference();
                    pref.setStudentId(student.getEnrollmentNumber());
                    pref.setJobListing(jobListing);
                    pref.setPreferenceOrder(priority);
                    pref.setPreferenceId(student.getEnrollmentNumber() + "_" + jobListing.getListingId());

                    preferenceRepo.save(pref);
                }
            }
        });

        return "Preferences saved successfully!";
    }
}
