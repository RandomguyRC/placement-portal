package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    boolean existsByStudentIdAndListingId(String studentId, String listingId);
    List<Application> findByStudentId(String studentId);
    
    List<Application> findByListingIdAndWillingnessTrueOrderByAppliedAt(String listingId);
    List<Application> findByListingId(String listingId);
}