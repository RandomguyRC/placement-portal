package com.placementportal.placement_website.service;

import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.repository.TprRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TprService {

    private final TprRepository tprRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public TprService(TprRepository tprRepository) {
        this.tprRepository = tprRepository;
    }

    // ---------------- REGISTER TPR ----------------
    public String register(Tpr tpr) {
        // Check if email exists
        Optional<Tpr> existingTpr = tprRepository.findByEmail(tpr.getEmail());
        if (existingTpr.isPresent()) {
            return "Email already registered!";
        }

        // 🔐 Hash password before saving
        tpr.setPassword(passwordEncoder.encode(tpr.getPassword()));

        int result = tprRepository.save(tpr);
        return result > 0 ? "Signup successful!" : "Signup failed!";
    }

    // ---------------- FIND TPR BY EMAIL ----------------
    public Optional<Tpr> findByEmail(String email) {
        return tprRepository.findByEmail(email);
    }
}
