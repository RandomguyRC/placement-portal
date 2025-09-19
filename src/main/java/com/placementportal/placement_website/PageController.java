package com.placementportal.placement_website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    @GetMapping("/")
    public String homepage() {
        return "homepage"; // homepage.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    // ---------------- MOCK LOGIN HANDLERS ----------------

    @PostMapping("/login/student")
    public String loginStudent(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        // Hardcoded student login
        if ("student@college.edu".equals(username) && "pass".equals(password)) {
            model.addAttribute("username", username);
            return "redirect:/profile"; // student.html
        } else {
            model.addAttribute("error", "Invalid student credentials");
            return "login"; // back to login.html
        }
    }

    @PostMapping("/login/tpr")
    public String loginTpr(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        // Hardcoded TPR login
        if ("tpr@college.edu".equals(username) && "1234".equals(password)) {
            model.addAttribute("username", username);
            return "redirect:/tpr-profile"; // tpr_profile.html
        } else {
            model.addAttribute("error", "Invalid TPR credentials");
            return "login"; // back to login.html
        }
    }

    // -----------------------------------------------------

    @GetMapping("/signup")
    public String signup() {
        return "signup"; // signup.html
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile"; // profile.html
    }

    @GetMapping("/companies")
    public String companies() {
        return "company-listing"; // company-listing.html
    }

    @GetMapping("/student")
    public String student() {
        return "student"; // student.html
    }

    @GetMapping("/tpr-profile")
    public String tprProfile() {
        return "tpr_profile"; // tpr_profile.html
    }

    @GetMapping("/notices")
    public String notices() {
        return "notices"; // notices.html
    }

    @GetMapping("/opportunities")
    public String opportunities() {
        return "opportunities"; // opportunities.html
    }

    @GetMapping("/preferences")
    public String preferences() {
        return "preferences"; // preferences.html
    }

    @GetMapping("/resources")
    public String resources() {
        return "resources"; // resources.html
    }

    @GetMapping("/schedule")
    public String schedule() {
        return "schedule_tpr"; // schedule_tpr.html
    }

    @GetMapping("/alerts")
    public String alerts() {
        return "alert_notifications"; // alert_notifications.html
    }

     @GetMapping("/sittingplan")
    public String sittingplan() {
        return "sitting-plan"; // 
    }

    @GetMapping("/mytoken")
    public String mytoken() {
        return "mytoken"; //
    }

    @GetMapping("/tprtoken")
    public String tprtoken() {
        return "tprtoken"; //
    }
}
