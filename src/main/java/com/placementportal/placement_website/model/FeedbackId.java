package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FeedbackId implements Serializable {

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "assessment_id")
    private String assessmentId;

    public FeedbackId() {}

    public FeedbackId(String studentId, String assessmentId) {
        this.studentId = studentId;
        this.assessmentId = assessmentId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeedbackId that)) return false;
        return Objects.equals(studentId, that.studentId) &&
               Objects.equals(assessmentId, that.assessmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, assessmentId);
    }
}