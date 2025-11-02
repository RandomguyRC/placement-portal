package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Feedback;
import com.placementportal.placement_website.model.FeedbackId;
import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.model.Assessment;
import com.placementportal.placement_website.repository.FeedbackRepository;
import com.placementportal.placement_website.repository.AssessmentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@SessionAttributes({"student", "tpr"})
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    // ✅ Show all feedbacks (with optional search)
    @GetMapping("/feedback")
    public String getAllFeedback(@RequestParam(value = "search", required = false) String search,
                                 Model model, HttpSession session) {

        Object studentObj = session.getAttribute("student");
        Object tprObj = session.getAttribute("tpr");
        String role = (tprObj != null) ? "TPR" : (studentObj != null ? "STUDENT" : null);
        if (role == null) return "redirect:/login";

        List<Feedback> feedbackList;
        if (search != null && !search.isEmpty()) {
            feedbackList = feedbackRepository.findById_AssessmentId(search);
        } else {
            feedbackList = feedbackRepository.findAll();
        }

        List<Assessment> assessmentList = assessmentRepository.findAll();

        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("assessmentList", assessmentList);
        model.addAttribute("role", role);
        model.addAttribute("searchQuery", search);

        return "feedback";
    }

    // ✅ Add new feedback
    @PostMapping("/feedback/add")
    public String addFeedback(@RequestParam String assessmentId,
                              @RequestParam String description,
                              HttpSession session) {

        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return "redirect:/login";
        }

        FeedbackId id = new FeedbackId(student.getEnrollmentNumber(), assessmentId);
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setDescription(description);
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(LocalDateTime.now());
        feedback.setUpvotes(0);
        feedback.setDownvotes(0);

        feedbackRepository.save(feedback);
        return "redirect:/feedback";
    }

    @PostMapping("/feedback/upvote")
    public String upvoteFeedback(@RequestParam String studentId,
                                 @RequestParam String assessmentId) {
        FeedbackId id = new FeedbackId(studentId, assessmentId);
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (feedback != null) {
            feedback.setUpvotes(feedback.getUpvotes() + 1);
            feedback.setUpdatedAt(LocalDateTime.now());
            feedbackRepository.save(feedback);
        }
        return "redirect:/feedback";
    }

    @PostMapping("/feedback/downvote")
    public String downvoteFeedback(@RequestParam String studentId,
                                   @RequestParam String assessmentId) {
        FeedbackId id = new FeedbackId(studentId, assessmentId);
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (feedback != null) {
            feedback.setDownvotes(feedback.getDownvotes() + 1);
            feedback.setUpdatedAt(LocalDateTime.now());
            feedbackRepository.save(feedback);
        }
        return "redirect:/feedback";
    }
}
