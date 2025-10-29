package com.placementportal.placement_website.service;

import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.repository.TprRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TprService {

    private final TprRepository tprRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public TprService(TprRepository tprRepository) {
        this.tprRepository = tprRepository;
    }

    // ---------------- REGISTER TPR ----------------
    public String register(Tpr tpr) {
        // 1️⃣ Validate TPR ID
        if (tpr.getTprId() == null || tpr.getTprId().trim().isEmpty()) {
            return "Error: TPR ID is required!";
        }

        // 2️⃣ Check for existing email
        if (tprRepository.findByEmail(tpr.getEmail()) != null) {
            return "Error: Email already registered!";
        }

        // 3️⃣ Default role if not set
        if (tpr.getRole() == null) {
            tpr.setRole(Tpr.Role.TPR);
        }

        // 4️⃣ Hash password before saving
        tpr.setPassword(passwordEncoder.encode(tpr.getPassword()));

        // 5️⃣ Save to DB
        int result = tprRepository.save(tpr);
        return result > 0 ? "Signup successful!" : "Error: Signup failed!";
    }
}
