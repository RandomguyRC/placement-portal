package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Notification;
import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.NotificationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    // Page to render notifications (full page)
    @GetMapping("/notifications")
    public String showNotifications(HttpSession session, Model model) {
        Object studentObj = session.getAttribute("student");
        Object tprObj = session.getAttribute("tpr");

        if (studentObj == null && tprObj == null) {
            return "redirect:/login";
        }

        model.addAttribute("student", studentObj);
        model.addAttribute("tpr", tprObj);

        if (studentObj != null) {
            Student student = (Student) studentObj;
            List<Notification> notifications =
                    notificationRepository.findByStudentEnrollmentNumber(student.getEnrollmentNumber());

            model.addAttribute("notifications", notifications);
        } else {
            model.addAttribute("notifications", List.of());
        }

        return "notifications";
    }

    // API endpoint for dynamic fetch (JSON)
    @GetMapping("/api/notifications")
    @ResponseBody
    public List<Notification> getNotifications(@RequestParam String studentId) {
        return notificationRepository.findByStudentEnrollmentNumber(studentId);
    }
}
