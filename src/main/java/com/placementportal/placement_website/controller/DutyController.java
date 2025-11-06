package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Duty;
import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.service.DutyService;
import com.placementportal.placement_website.service.TprService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/duties")
public class DutyController {

    @Autowired
    private DutyService dutyService;

    @Autowired
    private TprService tprService;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Show the logged-in TPR’s duties.
     * Works for both Principal-based login and session-based login.
     */
    @GetMapping
    public String showTprDuties(Principal principal, HttpSession session, Model model) {

        Tpr currentTpr = null;

        // 1️⃣ If logged in via Spring Security
        if (principal != null) {
            String email = principal.getName();
            Optional<Tpr> optionalTpr = tprService.findByEmail(email);
            if (optionalTpr.isPresent()) {
                currentTpr = optionalTpr.get();
                session.setAttribute("tpr", currentTpr); // sync with session
            }
        }

        // 2️⃣ If not via Principal, check session
        if (currentTpr == null) {
            Object sessObj = session.getAttribute("tpr");
            if (sessObj instanceof Tpr) {
                currentTpr = (Tpr) sessObj;
            }
        }

        // 3️⃣ If still null → redirect to login
        if (currentTpr == null) {
            return "redirect:/login";
        }

        // 4️⃣ Fetch joined duty details (company, job role, venue, timing)
        List<Map<String, Object>> duties = dutyService.getDutyDetailsByTpr(currentTpr.getTprId());

        // 5️⃣ Pass data to the view
        model.addAttribute("tpr", currentTpr);
        model.addAttribute("duties", duties);

        return "duties"; // Render duties.html
    }

    /**
     * Admin: Assign duties for a specific assessment.
     */
    @PostMapping("/assign/{assessmentId}")
    @ResponseBody
    public String assignDuties(@PathVariable String assessmentId) {
        dutyService.assignDuties(assessmentId);
        return "Duties assigned successfully for assessment: " + assessmentId;
    }
    /**
 * Fetches the assessment_id linked to a duty (used for attendance).
 */
@GetMapping("/{dutyId}/assessment")
@ResponseBody
public ResponseEntity<String> getAssessmentIdByDuty(@PathVariable String dutyId) {
    String sql = """
        SELECT a.assessment_id
        FROM duties d
        JOIN job_listings j ON d.related_listing_id = j.listing_id
        JOIN assessment a ON a.listing_id = j.listing_id
        WHERE d.duty_id = ?
    """;

    try {
        String assessmentId = jdbcTemplate.queryForObject(sql, String.class, dutyId);
        return ResponseEntity.ok(assessmentId);
    } catch (EmptyResultDataAccessException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No assessment found for this duty.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Error fetching assessment: " + e.getMessage());
    }
}

}