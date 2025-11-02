package com.placementportal.placement_website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.model.Tpr;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {


    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    
//     @GetMapping("/companies")
// public String companies(HttpSession session, Model model) {
//     Object student = session.getAttribute("student");
//     Object tpr = session.getAttribute("tpr");

//     if (student == null && tpr == null) {
//             return "redirect:/login";
//         }

//     model.addAttribute("student", student);
//     model.addAttribute("tpr", tpr);

//     return "company-listing"; // company-listing.html
// }


    @GetMapping("/student")
    public String studentPage(HttpSession session, Model model) {
        // Read session data
        Student student = (Student) session.getAttribute("student");
        Tpr tpr = (Tpr) session.getAttribute("tpr");

        // Add to Thymeleaf model so th:if can see them
        model.addAttribute("student", student);
        model.addAttribute("tpr", tpr);

        // ✅ Redirect if not logged in
        if (student == null && tpr == null) {
            return "redirect:/login";
        }

        // ✅ Return the Thymeleaf view (student.html)
        return "student";
    }

    // @GetMapping("/tpr-profile")
    // public String tprProfile() {
    //     return "tpr_profile"; // tpr_profile.html
    // }

    // @GetMapping("/notices")
    // public String notices() {
    //     return "notices"; // notices.html
    // }

    // @GetMapping("/opportunities")
    // public String opportunities() {
    //     return "opportunities"; // opportunities.html
    // }

    // @GetMapping("/preferences")
    // public String preferences() {
    //     return "preferences"; // preferences.html
    // }

    // @GetMapping("/resources")
    // public String resources() {
    //     return "resources"; // resources.html
    // }
    @GetMapping("/resources")
    public String resources(HttpSession session, Model model) {
        // Read session data
        Student student = (Student) session.getAttribute("student");
        Tpr tpr = (Tpr) session.getAttribute("tpr");

        // Add to Thymeleaf model so th:if can see them
        model.addAttribute("student", student);
        model.addAttribute("tpr", tpr);

        // ✅ Redirect if not logged in

        // ✅ Return the Thymeleaf view (student.html)
        return "resources";
    }

    @GetMapping("/schedule")
public String scheduleTprPage(HttpSession session, Model model) {
    // pull from session and put into Thymeleaf model
    Object student = session.getAttribute("student");
    Object tpr = session.getAttribute("tpr");

    model.addAttribute("student", student);
    model.addAttribute("tpr", tpr);

    return "schedule_tpr"; // corresponds to schedule_tpr.html
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

    // @GetMapping("/tprtoken")
    // public String tprtoken() {
    //     return "tprtoken"; //
    // }
    @GetMapping("/tprtoken")
    public String tprtoken(HttpSession session, Model model) {
        // Read session data
        Student student = (Student) session.getAttribute("student");
        Tpr tpr = (Tpr) session.getAttribute("tpr");

        // Add to Thymeleaf model so th:if can see them
        model.addAttribute("student", student);
        model.addAttribute("tpr", tpr);

        // ✅ Redirect if not logged in
        if (student == null && tpr == null) {
            return "redirect:/login";
        }

        // ✅ Return the Thymeleaf view (student.html)
        return "tprtoken";
    }
}
