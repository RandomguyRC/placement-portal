package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Student;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.placementportal.placement_website.repository.StudentRepository;

@Controller
public class ProfileController {

    private final StudentRepository studentRepository;

    public ProfileController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Render profile page
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");

        if (student == null) {
            return "redirect:/login";
        }

        model.addAttribute("student", student);
        return "profile";
    }

    // Handle profile update
    @PostMapping("/profile/update")
public String updateProfile(@ModelAttribute Student updatedStudent, HttpSession session, Model model) {
    Student student = (Student) session.getAttribute("student");

    if (student == null) {
        return "redirect:/login";
    }

    // Check if email is changing
    if (!student.getEmail().equals(updatedStudent.getEmail())) {
        // Check if new email already exists
        if (studentRepository.emailExists(updatedStudent.getEmail())) {
            model.addAttribute("student", student);
            model.addAttribute("errorMessage", "Email already in use! Please choose another.");
            return "profile"; // stay on profile page
        }
    }

    // Update editable fields
    student.setEmail(updatedStudent.getEmail());
    student.setPhoneNumber(updatedStudent.getPhoneNumber());
    student.setDob(updatedStudent.getDob());
    student.setSex(updatedStudent.getSex());
    student.setCpi(updatedStudent.getCpi());
    student.setAddress(updatedStudent.getAddress());
    student.setLocalAddress(updatedStudent.getLocalAddress());

    // Persist to DB
    studentRepository.update(student); // <-- you will need to add an update() method in StudentRepository

    // Update session
    session.setAttribute("student", student);
    model.addAttribute("student", student);
    model.addAttribute("successMessage", "Profile updated successfully!");

    return "profile";
}

}
