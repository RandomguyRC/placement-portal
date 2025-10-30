package com.placementportal.placement_website.service;

import com.placementportal.placement_website.model.Application;
import com.placementportal.placement_website.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public void saveApplication(Application application) {
        // Set default values if not already set
        if (application.getApplicationId() == null || application.getApplicationId().isEmpty()) {
            application.setApplicationId(UUID.randomUUID().toString());
        }

        if (application.getAppliedAt() == null) {
            application.setAppliedAt(new Date());
        }

        applicationRepository.save(application);
    }
}
