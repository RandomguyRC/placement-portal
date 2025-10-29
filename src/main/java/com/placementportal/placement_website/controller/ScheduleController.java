package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.dto.dto.UpcomingAssessmentDTO;
import com.placementportal.placement_website.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/{enrollmentNumber}")
    public Map<String, Object> getSchedule(@PathVariable String enrollmentNumber) {
        List<UpcomingAssessmentDTO> schedules = scheduleService.getUpcomingAssessments(enrollmentNumber);
        Map<String, Object> resp = new HashMap<>();
        resp.put("schedules", schedules);
        return resp;
    }
}
