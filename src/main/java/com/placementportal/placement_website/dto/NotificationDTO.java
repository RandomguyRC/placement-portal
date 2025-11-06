package com.placementportal.placement_website.dto;

import java.time.LocalDateTime;

public class NotificationDTO {
    private String notificationId;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDTO(String notificationId, String message, boolean isRead, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters
    public String getNotificationId() { return notificationId; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
