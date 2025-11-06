package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.dto.SittingPlanDTO;
import com.placementportal.placement_website.service.SittingPlanService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sitting-plan")
@CrossOrigin(origins = "*")
public class SittingPlanController {

    private final SittingPlanService sittingPlanService;

    public SittingPlanController(SittingPlanService sittingPlanService) {
        this.sittingPlanService = sittingPlanService;
    }

    // generate and save plan
    @PostMapping("/generate/{assessmentId}")
    public SittingPlanDTO generatePlan(@PathVariable String assessmentId) {
        return sittingPlanService.generateAndSavePlan(assessmentId);
    }

    // just fetch existing plan json (if you want)
    // GET /api/sitting-plan/{assessmentId}
    @GetMapping("/{assessmentId}")
    public SittingPlanDTO getPlan(@PathVariable String assessmentId) {
        // either return saved JSON parsed to DTO or regenerate
        return sittingPlanService.generateAndSavePlan(assessmentId); // regenerate for now
    }
}