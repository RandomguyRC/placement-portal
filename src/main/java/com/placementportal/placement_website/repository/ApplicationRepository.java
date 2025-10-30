package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    boolean existsByStudentIdAndListingId(String studentId, String listingId);
}
