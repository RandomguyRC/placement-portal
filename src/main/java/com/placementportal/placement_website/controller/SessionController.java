package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.model.Tpr;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/student")
    public ResponseEntity<?> getLoggedInStudent(HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/tpr")
    public ResponseEntity<?> getLoggedInTpr(HttpSession session) {
        Tpr tpr = (Tpr) session.getAttribute("tpr");
        if (tpr == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        return ResponseEntity.ok(tpr);
    }
}
