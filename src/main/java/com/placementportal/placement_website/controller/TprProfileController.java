package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.repository.TprRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TprProfileController {

    private final TprRepository tprRepository;

    public TprProfileController(TprRepository tprRepository) {
        this.tprRepository = tprRepository;
    }

    // Render TPR profile page
    @GetMapping("/tpr-profile")
    public String profilePage(HttpSession session, Model model) {
        Tpr tpr = (Tpr) session.getAttribute("tpr");

        if (tpr == null) {
            return "redirect:/login";
        }

        model.addAttribute("tpr", tpr);
        return "tpr-profile"; // Thymeleaf template
    }

    // Handle profile update
    @PostMapping("/tpr-profile/update")
    public String updateProfile(@ModelAttribute Tpr updatedTpr, HttpSession session, Model model) {
        Tpr tpr = (Tpr) session.getAttribute("tpr");

        if (tpr == null) {
            return "redirect:/login";
        }

        // Check if email is changing
        if (!tpr.getEmail().equals(updatedTpr.getEmail())) {
            if (tprRepository.findByEmail(updatedTpr.getEmail()) != null) {
                model.addAttribute("tpr", tpr);
                model.addAttribute("errorMessage", "Email already in use! Please choose another.");
                return "tpr-profile";
            }
        }

        // Update editable fields only
        tpr.setEmail(updatedTpr.getEmail());
        tpr.setPhoneNumber(updatedTpr.getPhoneNumber());
        tpr.setLocalAddress(updatedTpr.getLocalAddress());
        tpr.setBranch(updatedTpr.getBranch());
        tpr.setSex(updatedTpr.getSex());
        // ❌ role not editable

        // Persist to DB
        tprRepository.update(tpr);

        // Update session
        session.setAttribute("tpr", tpr);
        model.addAttribute("tpr", tpr);
        model.addAttribute("successMessage", "Profile updated successfully!");

        return "tpr-profile";
    }
}
