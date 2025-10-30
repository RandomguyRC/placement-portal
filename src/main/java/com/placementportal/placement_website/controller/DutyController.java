package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Duty;
import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.service.DutyService;
import com.placementportal.placement_website.service.TprService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/duties")
public class DutyController {

    @Autowired
    private DutyService dutyService;

    @Autowired
    private TprService tprService;

    /**
     * Redirect to the logged-in TPR’s duties page.
     * Uses Principal if available; otherwise falls back to session attribute "tpr".
     */
    @GetMapping
    public String redirectToTprDuties(Principal principal, HttpSession session) {
        // 1) Try Principal (Spring Security)
        if (principal != null) {
            String email = principal.getName();
            Tpr tpr = tprService.findByEmail(email); // returns Tpr or null
            if (tpr != null) {
                return "redirect:/duties/tpr/" + tpr.getTprId();
            }
        }

        // 2) Fallback: check session (your login stores TPR in session under "tpr")
        Object sessObj = session.getAttribute("tpr");
        if (sessObj instanceof Tpr) {
            Tpr sessionTpr = (Tpr) sessObj;
            return "redirect:/duties/tpr/" + sessionTpr.getTprId();
        }

        // 3) Not logged in as TPR -> go to login
        return "redirect:/login";
    }

    /**
     * Show duties for a specific TPR.
     */
    @GetMapping("/tpr/{tprId}")
    public String getDutiesForTpr(@PathVariable String tprId, Model model) {
        List<Duty> duties = dutyService.getDutiesByTpr(tprId);
        model.addAttribute("duties", duties);
        return "duties"; // renders duties.html
    }

    /**
     * Assign duties for an assessment (Admin function).
     */
    @PostMapping("/assign/{assessmentId}")
    @ResponseBody
    public String assignDuties(@PathVariable String assessmentId) {
        dutyService.assignDuties(assessmentId);
        return "Duties assigned successfully for assessment: " + assessmentId;
    }
}
