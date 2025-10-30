package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.repository.StudentRepository;
import com.placementportal.placement_website.repository.TprRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TprRepository tprRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ---------------- STUDENT LOGIN ----------------
    @PostMapping("/login/student")
    public String loginStudent(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model,
                               HttpSession session) {

        Student student = studentRepository.findByEmail(email);

        if (student != null && passwordEncoder.matches(password, student.getPassword())) {
            // Login successful
            session.setAttribute("student", student);
            return "redirect:/";  // Home page on success
        } else {
            // Login failed
            model.addAttribute("error", "Invalid email or password");
            return "login";  // stay on login page
        }
    }

    // ---------------- TPR LOGIN ----------------
    @PostMapping("/login/tpr")
    public String loginTpr(@RequestParam("email") String email,
                           @RequestParam("password") String password,
                           Model model,
                           HttpSession session) {

        System.out.println("TPR login attempt");
        System.out.println("Email from form: " + email);
        System.out.println("Password from form: " + password);

        Optional<Tpr> optionalTpr = tprRepository.findByEmail(email);
        boolean found = optionalTpr.isPresent();
        System.out.println("TPR found in DB: " + found);

        if (found) {
            Tpr tpr = optionalTpr.get();
            System.out.println("Stored password in DB: " + tpr.getPassword());
            System.out.println("Password matches: " + passwordEncoder.matches(password, tpr.getPassword()));

            if (passwordEncoder.matches(password, tpr.getPassword())) {
                // Login successful
                session.setAttribute("tpr", tpr);
                return "redirect:/tprhomepage";
                //return "redirect:/";  // Redirect to home page on success
            }
        }

        // Login failed
        model.addAttribute("error", "Invalid email or password");
        return "login";  // stay on login page
    }
@GetMapping("/tprhomepage")
    public String tprHomepage(HttpSession session, Model model) {
        Tpr tpr = (Tpr) session.getAttribute("tpr");
        if (tpr == null) {
            return "redirect:/login";
        }
        model.addAttribute("tpr", tpr);
        return "tprhomepage"; // render templates/tpr-homepage.html
    }
    // ---------------- STUDENT DASHBOARD ----------------
    @GetMapping("/student/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return "redirect:/login"; // not logged in
        }
        model.addAttribute("student", student);
        return "student-dashboard";
    }

    // ---------------- TPR DASHBOARD ----------------
    @GetMapping("/tpr/profile")
    public String tprProfile(HttpSession session, Model model) {
        Tpr tpr = (Tpr) session.getAttribute("tpr");
        if (tpr == null) {
            return "redirect:/login"; // not logged in
        }
        model.addAttribute("tpr", tpr);
        return "tpr-profile"; // create this Thymeleaf template
    }

    // ---------------- LOGOUT ----------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // clears both student and TPR sessions
        return "redirect:/login";
    }
}
