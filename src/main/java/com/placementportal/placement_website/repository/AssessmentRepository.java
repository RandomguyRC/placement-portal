package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, String> {
}
