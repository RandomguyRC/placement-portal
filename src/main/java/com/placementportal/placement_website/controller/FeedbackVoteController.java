package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Feedback;
import com.placementportal.placement_website.model.FeedbackId;
import com.placementportal.placement_website.model.FeedbackVote;
import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.FeedbackRepository;
import com.placementportal.placement_website.repository.FeedbackVoteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/feedback/vote")
public class FeedbackVoteController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackVoteRepository feedbackVoteRepository;

    @PostMapping("/{type}")
    public String handleVote(@PathVariable String type,
                             @RequestParam String studentId,
                             @RequestParam String assessmentId,
                             HttpSession session) {

        Student voter = (Student) session.getAttribute("student");
        if (voter == null) {
            return "redirect:/login";
        }

        // Check if already voted
        if (feedbackVoteRepository.findByVoterIdAndStudentIdAndAssessmentId(
                voter.getEnrollmentNumber(), studentId, assessmentId).isPresent()) {
            return "redirect:/feedback?error=already_voted";
        }

        // Save the vote
        boolean isUpvote = type.equals("upvote");
        feedbackVoteRepository.save(
                new FeedbackVote(voter.getEnrollmentNumber(), studentId, assessmentId, isUpvote)
        );

        // Update feedback counts
        FeedbackId id = new FeedbackId(studentId, assessmentId);
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (feedback != null) {
            if (isUpvote) {
                feedback.setUpvotes(feedback.getUpvotes() + 1);
            } else {
                feedback.setDownvotes(feedback.getDownvotes() + 1);
            }
            feedback.setUpdatedAt(LocalDateTime.now());
            feedbackRepository.save(feedback);
        }

        return "redirect:/feedback";
    }
}