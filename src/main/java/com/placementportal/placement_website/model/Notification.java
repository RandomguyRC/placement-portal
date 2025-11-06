package com.placementportal.placement_website.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "notifications")
@JsonIgnoreProperties({"recipient"})
public class Notification {

    @Id
    @Column(name = "notification_id", nullable = false, length = 255)
    private String notificationId;

    // 🎯 The recipient (linked to student_details.enrollment_number)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "enrollment_number")
    private Student recipient;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read")
    private boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ Constructors
    public Notification() {}

    public Notification(String notificationId, Student recipient, String message) {
        this.notificationId = notificationId;
        this.recipient = recipient;
        this.message = message;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    // ✅ Getters and Setters
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public Student getRecipient() {
        return recipient;
    }

    public void setRecipient(Student recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
