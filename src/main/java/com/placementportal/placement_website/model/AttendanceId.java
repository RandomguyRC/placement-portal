package com.placementportal.placement_website.model;

import java.io.Serializable;
import java.util.Objects;

public class AttendanceId implements Serializable {
    private String studentId;
    private String assessmentId;

    public AttendanceId() {}

    public AttendanceId(String studentId, String assessmentId) {
        this.studentId = studentId;
        this.assessmentId = assessmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceId)) return false;
        AttendanceId that = (AttendanceId) o;
        return Objects.equals(studentId, that.studentId) &&
               Objects.equals(assessmentId, that.assessmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, assessmentId);
    }
}
