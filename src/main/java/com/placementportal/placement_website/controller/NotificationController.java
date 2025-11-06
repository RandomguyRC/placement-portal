package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.dto.NotificationDTO;
import com.placementportal.placement_website.model.Notification;
import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.NotificationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
public List<NotificationDTO> getNotifications(HttpSession session) {
    Student student = (Student) session.getAttribute("student");
    if (student == null) {
        return List.of();
    }

    List<Notification> notifications =
            notificationRepository.findByStudentEnrollmentNumber(student.getEnrollmentNumber());

    return notifications.stream()
            .map(n -> new NotificationDTO(
                    n.getNotificationId(),
                    n.getMessage(),
                    n.isRead(),
                    n.getCreatedAt()
            ))
            .toList();
}

@PostMapping("/api/notifications/mark-read")
@ResponseBody
public String markNotificationAsRead(
        @RequestParam("id") String notificationId,
        HttpSession session) {

    Student student = (Student) session.getAttribute("student");
    if (student == null) {
        return "NOT_LOGGED_IN";
    }

    Notification notification = notificationRepository.findById(notificationId).orElse(null);

    if (notification == null) {
        return "NOT_FOUND";
    }

    // ✅ ensure the logged-in student owns this notification
    if (!notification.getRecipient().getEnrollmentNumber()
            .equals(student.getEnrollmentNumber())) {
        return "UNAUTHORIZED";
    }

    notification.setRead(true);
    notificationRepository.save(notification);

    return "OK";
}



@GetMapping("/api/debug-notifications")
@ResponseBody
public String debugNotifications() {
    List<Notification> notifications = notificationRepository.findAll();
    notifications.forEach(n -> {
        System.out.println(n.getNotificationId() + " -> " + n.getRecipient().getEnrollmentNumber());
    });
    return "Check console for output";
}

}
