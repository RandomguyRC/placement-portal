package com.placementportal.placement_website.model;

import java.time.LocalDateTime;

public class Token {

    private Long id;                  // AUTO_INCREMENT
    private String tokenId;           // e.g. TKN-<timestamp>
    private String studentId;         // FK → student_details(enrollment_number)
    private String title;             // Token title (short summary)
    private String description;       // Detailed description
    private String category;          // Category (e.g. "Technical", "Placement", etc.)
    private String status;            // pending / in_progress / resolved / closed
    private String priority;          // Low / Medium / High
    private String assignedTo;        // Admin/TPR handling the token
    private String tprReply;          // ✅ TPR reply message (only once)
    private LocalDateTime createdAt;  // Creation time
    private LocalDateTime updatedAt;  // Last update time
    private LocalDateTime resolvedAt; // ✅ When TPR closes/resolves the token

    // --- Constructors ---
    public Token() {}

    public Token(String studentId, String title, String description, String category, String priority) {
        this.tokenId = "TKN-" + System.currentTimeMillis();
        this.studentId = studentId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = "pending"; // lowercase to match DB ENUM-like usage
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTokenId() { return tokenId; }
    public void setTokenId(String tokenId) { this.tokenId = tokenId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getTprReply() { return tprReply; }
    public void setTprReply(String tprReply) { this.tprReply = tprReply; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
