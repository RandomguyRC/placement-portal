package com.placementportal.placement_website.service;

import com.placementportal.placement_website.dto.dto.UpcomingAssessmentDTO;
import com.placementportal.placement_website.repository.AssessmentNativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final AssessmentNativeRepository assessmentNativeRepository;

    public ScheduleService(AssessmentNativeRepository assessmentNativeRepository) {
        this.assessmentNativeRepository = assessmentNativeRepository;
    }

    public List<UpcomingAssessmentDTO> getUpcomingAssessments(String studentId) {
        List<Map<String, Object>> results = assessmentNativeRepository.findSchedulesByStudent(studentId);

        return results.stream().map(row -> new UpcomingAssessmentDTO(
                (String) row.get("assessment_id"),
                (String) row.get("type"),
                row.get("start_time") != null ? row.get("start_time").toString() : null,
                row.get("end_time") != null ? row.get("end_time").toString() : null,
                (String) row.get("company_name"),
                (String) row.get("job_role"),
                (String) row.get("mode_of_hiring")
        )).collect(Collectors.toList());
    }
}
