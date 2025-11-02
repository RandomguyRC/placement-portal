package com.placementportal.placement_website.repository;

import com.placementportal.placement_website.model.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, String> {

    // 🔹 Existing query (keep this)
    @Query("SELECT j FROM JobListing j JOIN FETCH j.company")
    List<JobListing> findAllWithCompany();

    // 🔹 New query to find job listings by company name
    List<JobListing> findByCompany_CompanyName(String companyName);
}
