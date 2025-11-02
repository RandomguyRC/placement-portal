package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Feedback;
import com.placementportal.placement_website.model.FeedbackId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, FeedbackId> {

    // ✅ New method to search feedbacks by assessment ID
    List<Feedback> findById_AssessmentId(String assessmentId);
}
