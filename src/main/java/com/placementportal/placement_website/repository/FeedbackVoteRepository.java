package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.FeedbackVote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FeedbackVoteRepository extends JpaRepository<FeedbackVote, Long> {

    Optional<FeedbackVote> findByVoterIdAndStudentIdAndAssessmentId(String voterId, String studentId, String assessmentId);
}