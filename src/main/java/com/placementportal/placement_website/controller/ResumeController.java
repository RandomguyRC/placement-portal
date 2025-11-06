package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Resume;
import com.placementportal.placement_website.model.Student;
import com.placementportal.placement_website.repository.ResumeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeRepository resumeRepository;

    public ResumeController(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    @PostMapping("/upload")
    public String uploadResume(@RequestBody Map<String, String> body, HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) return "Not logged in";

        String studentId = student.getEnrollmentNumber();
        String resumeFile = body.get("resumeFile");

        // ✅ generate resume_id like 2023BTECH001_1, 2023BTECH001_2, etc.
        int nextIndex = resumeRepository.countByStudentId(studentId) + 1;
        String resumeId = studentId + "_" + nextIndex;

        int rows = resumeRepository.save(resumeId, studentId, resumeFile, LocalDateTime.now(), "UNVERIFIED");
        return rows > 0 ? "Uploaded" : "Error";
    }

    @GetMapping("/all")
    public List<Resume> getAllResumes(HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) return List.of();

        return resumeRepository.findByStudentId(student.getEnrollmentNumber());
    }
    @DeleteMapping("/delete/{resumeId}")
public String deleteResume(@PathVariable String resumeId, HttpSession session) {
    Student student = (Student) session.getAttribute("student");
    if (student == null) return "Not logged in";

    int rows = resumeRepository.deleteByResumeIdAndStudentId(resumeId, student.getEnrollmentNumber());
    return rows > 0 ? "Deleted" : "Error";
}

@PutMapping("/verify/{resumeId}")
public String verifyResume(@PathVariable String resumeId, @RequestParam String status, HttpSession session) {
    Object tpr = session.getAttribute("tpr");
    if (tpr == null) return "Not authorized";

    int rows = resumeRepository.updateStatus(resumeId, status);
    return rows > 0 ? "Status updated" : "Error updating status";
}

@GetMapping("/all-admin")
public List<Resume> getAllResumesForTPR(HttpSession session) {
    Object tpr = session.getAttribute("tpr");
    if (tpr == null) return List.of(); // restrict access

    return resumeRepository.findAll();
}

@GetMapping("/admin/resumes")
public List<Resume> getResumesForStudent(@RequestParam("studentId") String studentId, HttpSession session) {
    Object tpr = session.getAttribute("tpr");
    if (tpr == null) return List.of(); // not authorized

    return resumeRepository.findByStudentId(studentId);
}
@GetMapping("/verified")
public List<Resume> getVerifiedResumes(HttpSession session) {
    Student student = (Student) session.getAttribute("student");
    if (student == null) return List.of();

    return resumeRepository.findVerifiedByStudentId(student.getEnrollmentNumber());
}


}