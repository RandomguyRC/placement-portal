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
import java.util.HashMap;
import java.util.Map;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private TprRepository tprRepository;

    // ✅ Show all notices
    @GetMapping("/notices")
        public String getAllNotices(Model model, HttpSession session) {
            String role = (String) session.getAttribute("role");
            if (role == null) {
                return "redirect:/login";
            }

            List<Notice> notices = noticeRepository.findAll();

            // Fetch the TPR name for each notice using posted_by (tpr_id)
            for (Notice notice : notices) {
                String tprId = notice.getPostedBy();
                String tprName = tprRepository.findNameById(tprId);
                notice.setPostedBy(tprName); // temporarily replace ID with name for display
            }

            model.addAttribute("notices", notices);
            model.addAttribute("role", role);
            return "notices";
        }


    @PostMapping("/notices/add")
public String addNotice(@RequestParam String title,
                        @RequestParam String content,
                        HttpSession session) {

    String role = (String) session.getAttribute("role");
    if (role == null || !role.equals("TPR")) {
        return "redirect:/login";
    }

    Tpr tpr = (Tpr) session.getAttribute("tpr");
    if (tpr == null) {
        return "redirect:/login";
    }

    Notice notice = new Notice();

    // ✅ Assign a unique ID manually (important)
    notice.setNoticeId(java.util.UUID.randomUUID().toString());

    notice.setTitle(title);
    notice.setContent(content);

    notice.setPostedBy(tpr.getTprId()); // ✅ store ID, not name

    notice.setPostedAt(java.time.LocalDateTime.now());

    noticeRepository.save(notice);

    return "redirect:/notices";
}


    // ✅ Delete notice (TPR only)
    @PostMapping("/notices/delete/{id}")
    public String deleteNotice(@PathVariable String id, HttpSession session) {
        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("TPR")) {
            return "redirect:/login";
        }

        noticeRepository.deleteById(id);
        return "redirect:/notices";
    }
}
