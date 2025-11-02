package com.placementportal.placement_website.service;

import com.placementportal.placement_website.model.Assessment;
import com.placementportal.placement_website.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepo;

    // Fetch interviews for a specific date
    public List<Assessment> getInterviewsForStudentOnDate(String studentId, LocalDate date) {
        return assessmentRepo.findByInterviewDateAndStudent(date, studentId);
    }

    // ✅ Fetch upcoming interviews (future)
    public List<Assessment> getUpcomingInterviews(String studentId, LocalDate currentDate) {
        return assessmentRepo.findUpcomingInterviews(studentId, currentDate);
    }
}
