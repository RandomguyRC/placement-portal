package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface JobListingRepository extends JpaRepository<JobListing, String> {

    @Query("SELECT j FROM JobListing j JOIN FETCH j.company")
    List<JobListing> findAllWithCompany();

    // Later: you can add a query for eligible listings if needed
}
