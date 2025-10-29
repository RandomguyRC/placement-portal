package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Token;
import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.TokenRepository;
import com.placementportal.placement_website.repository.StudentRepository;
import com.placementportal.placement_website.service.EmailService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private StudentRepository studentRepository; // ✅ added

    @Autowired
    private EmailService emailService;  // ✅ Email service


    // ---------------------- STUDENT ENDPOINTS ----------------------

    // ✅ Get all tokens for a specific student
    @GetMapping("/{studentId}")
    public List<Token> getTokensByStudent(@PathVariable String studentId) {
        return tokenRepository.findByStudentId(studentId);
    }

    // ✅ Create new token
    @PostMapping("/create")
    public ResponseEntity<?> createToken(@RequestBody Token token, HttpSession session) {
        try {
            Object studentObj = session.getAttribute("student");
            if (studentObj == null) {
                return ResponseEntity.status(401).body("Unauthorized: Please log in first.");
            }

            // Extract student info from session
            String studentId, studentEmail, studentName;
            try {
                studentId = (String) studentObj.getClass().getMethod("getEnrollmentNumber").invoke(studentObj);
                studentEmail = (String) studentObj.getClass().getMethod("getEmail").invoke(studentObj);
                studentName = (String) studentObj.getClass().getMethod("getStudentName").invoke(studentObj);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Failed to extract student info from session.");
            }

            token.setStudentId(studentId);
            token.setTokenId("TKN-" + System.currentTimeMillis());
            token.setStatus("Pending");
            token.setCreatedAt(LocalDateTime.now());
            token.setUpdatedAt(LocalDateTime.now());

            int result = tokenRepository.save(token);
            if (result > 0) {
                // ✅ Send creation email
                try {
                    String subject = "🎫 New Support Token Created - " + token.getTokenId();
                    String body = "Dear " + studentName + ",\n\n"
                            + "Your support token has been created successfully.\n\n"
                            + "📄 Token Details:\n"
                            + "• Token ID: " + token.getTokenId() + "\n"
                            + "• Title: " + token.getTitle() + "\n"
                            + "• Category: " + token.getCategory() + "\n"
                            + "• Priority: " + token.getPriority() + "\n"
                            + "• Status: " + token.getStatus() + "\n\n"
                            + "You can track this token anytime on the TPC Portal.\n\n"
                            + "Regards,\nTPC Support Team";

                    emailService.sendEmail(studentEmail, subject, body);
                } catch (Exception e) {
                    System.err.println("⚠️ Failed to send token creation email: " + e.getMessage());
                }

                return ResponseEntity.ok("Token created successfully");
            } else {
                return ResponseEntity.internalServerError().body("Failed to insert token into DB");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }


    // ✅ Update token status
    @PutMapping("/{id}/update-status")
    public ResponseEntity<?> updateTokenStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            int result = tokenRepository.updateStatus(id, status);
            if (result > 0)
                return ResponseEntity.ok("Status updated successfully");
            else
                return ResponseEntity.badRequest().body("Token not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }



    // ---------------------- TPR ENDPOINTS ----------------------

    // ✅ Get all tokens (TPR view)
    @GetMapping("/all")
    public List<Token> getAllTokens() {
        return tokenRepository.findAll();
    }

    // ✅ TPR reply & mark resolved + email notification
    @PostMapping("/{id}/reply")
    public ResponseEntity<?> replyToToken(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            HttpSession session) {
        try {
            Optional<Token> tokenOpt = tokenRepository.findById(id);
            if (tokenOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Token not found");
            }

            Token token = tokenOpt.get();
            String reply = request.get("reply");

            // 🧠 Identify replying TPR
            String assignedTo = null;
            Object tprObj = session.getAttribute("tpr");
            if (tprObj != null) {
                try {
                    assignedTo = (String) tprObj.getClass().getMethod("getEmail").invoke(tprObj);
                } catch (Exception e) {
                    assignedTo = "TPR";
                }
            } else {
                assignedTo = request.getOrDefault("assignedTo", "TPR");
            }

            LocalDateTime now = LocalDateTime.now();

            int result = tokenRepository.updateReplyStatusAndAssign(
                    id,
                    reply,
                    "Resolved",
                    assignedTo,
                    now
            );

            if (result > 0) {
                // ✅ Send resolution email to the student
                try {
                    Student student = studentRepository.findByEnrollmentNumber(token.getStudentId());
                    if (student != null && student.getEmail() != null) {
                        String subject = "✅ Token Resolved - " + token.getTokenId();
                        String body = "Dear " + student.getStudentName() + ",\n\n"
                                + "Your support token has been resolved by the TPR.\n\n"
                                + "📄 Token Details:\n"
                                + "• Token ID: " + token.getTokenId() + "\n"
                                + "• Title: " + token.getTitle() + "\n"
                                + "• Category: " + token.getCategory() + "\n"
                                + "• Priority: " + token.getPriority() + "\n"
                                + "• Status: Resolved\n"
                                + "• Replied By: " + assignedTo + "\n"
                                + "• Reply Message: " + reply + "\n"
                                + "• Resolved At: " + now + "\n\n"
                                + "You can view this token anytime on the TPC Portal.\n\n"
                                + "Regards,\nTPC Support Team";

                        emailService.sendEmail(student.getEmail(), subject, body);
                    } else {
                        System.err.println("⚠️ No student email found for " + token.getStudentId());
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Failed to send resolution email: " + e.getMessage());
                }

                return ResponseEntity.ok("Reply added and token resolved successfully");
            } else {
                return ResponseEntity.internalServerError().body("Failed to update token");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
