package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.StudentPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentPreferenceRepository extends JpaRepository<StudentPreference, String> {
}
