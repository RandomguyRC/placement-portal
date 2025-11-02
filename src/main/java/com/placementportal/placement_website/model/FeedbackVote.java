package com.placementportal.placement_website.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback_vote",
       uniqueConstraints = @UniqueConstraint(columnNames = {"voter_id", "student_id", "assessment_id"}))
public class FeedbackVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who is giving the vote (the logged-in student)
    @Column(name = "voter_id")
    private String voterId;

    // Composite key pieces to identify the feedback being voted on
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "assessment_id")
    private String assessmentId;

    @Column(name = "is_upvote")
    private boolean isUpvote;

    // Constructors
    public FeedbackVote() {}

    public FeedbackVote(String voterId, String studentId, String assessmentId, boolean isUpvote) {
        this.voterId = voterId;
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.isUpvote = isUpvote;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public boolean isUpvote() {
        return isUpvote;
    }

    public void setUpvote(boolean upvote) {
        isUpvote = upvote;
    }
}
