package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice_board")
public class Notice {

    @Id
    private String noticeId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String attachments;

    @Column(name = "posted_by")
    private String postedBy;

    private LocalDateTime postedAt;

    // Getters and setters
    public String getNoticeId() { return noticeId; }
    public void setNoticeId(String noticeId) { this.noticeId = noticeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }

    public String getPostedBy() { return postedBy; }
    public void setPostedBy(String postedBy) { this.postedBy = postedBy; }

    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }
}