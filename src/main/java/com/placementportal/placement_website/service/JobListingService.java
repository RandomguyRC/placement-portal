package com.placementportal.placement_website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.placementportal.placement_website.model.JobListing;
import com.placementportal.placement_website.repository.JobListingRepository;
import java.util.List;

@Service
public class JobListingService {

    @Autowired
    private JobListingRepository jobListingRepository;

    public List<JobListing> getAllJobListings() {
        return jobListingRepository.findAll();
    }

    public JobListing getJobListingById(String listingId) {
        return jobListingRepository.findById(listingId).orElse(null);
    }
}
