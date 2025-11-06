package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.SittingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SittingPlanRepository extends JpaRepository<SittingPlan, String> {
    Optional<SittingPlan> findByAssessmentId(String assessmentId);
}
