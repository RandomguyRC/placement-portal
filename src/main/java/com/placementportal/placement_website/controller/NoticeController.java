package com.placementportal.placement_website.controller;

import com.placementportal.placement_website.model.Notice;
import com.placementportal.placement_website.model.Tpr;
import com.placementportal.placement_website.repository.NoticeRepository;
import com.placementportal.placement_website.repository.TprRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Comparator;


@Controller
@SessionAttributes({"student", "tpr"})
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private TprRepository tprRepository;


    // ✅ Show all notices
    @GetMapping("/notices")
    public String getAllNotices(Model model, HttpSession session) {
        Object studentObj = session.getAttribute("student");
        Object tprObj = session.getAttribute("tpr");

        // determine role
        String role = (tprObj != null) ? "TPR" : (studentObj != null ? "STUDENT" : null);

        if (role == null) {
            return "redirect:/login";
        }

        List<Notice> notices = noticeRepository.findAll()
        .stream()
        .sorted(Comparator.comparing(Notice::getPostedAt).reversed())
        .toList();


        // Fetch the TPR name or email for display
        for (Notice notice : notices) {
            String tprId = notice.getPostedBy();
            String tprNameOrEmail = tprRepository.findEmailById(tprId); // ✅ change to email
            if (tprNameOrEmail == null) {
                tprNameOrEmail = tprRepository.findNameById(tprId);
            }
            notice.setPostedBy(tprNameOrEmail != null ? tprNameOrEmail : "Unknown TPR");
        }

        model.addAttribute("notices", notices);
        model.addAttribute("role", role);
        return "notices";
    }


    // ✅ Add new notice (TPR only)
    @PostMapping("/notices/add")
    public String addNotice(@RequestParam String title,
                            @RequestParam String content,
                            HttpSession session) {

        Tpr tpr = (Tpr) session.getAttribute("tpr");
        if (tpr == null) {
            return "redirect:/login";
        }

        Notice notice = new Notice();
        notice.setNoticeId(java.util.UUID.randomUUID().toString());
        notice.setTitle(title);
        notice.setContent(content);
        notice.setPostedBy(tpr.getTprId()); // keep TPR ID for backend reference
        notice.setPostedAt(java.time.LocalDateTime.now());

        noticeRepository.save(notice);
        return "redirect:/notices";
    }


    // ✅ Delete notice (TPR only)
    @PostMapping("/notices/delete/{id}")
    public String deleteNotice(@PathVariable String id, HttpSession session) {
        Tpr tpr = (Tpr) session.getAttribute("tpr");
        if (tpr == null) {
            return "redirect:/login";
        }

        noticeRepository.deleteById(id);
        return "redirect:/notices";
    }
}
