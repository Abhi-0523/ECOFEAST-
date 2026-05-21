package com.ecofeast.model;

import java.time.LocalDateTime;

public class Notification {
    private int notificationId;
    private int userId;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private String linkUrl;
    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(int userId, String title, String message, String type, String linkUrl) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.linkUrl = linkUrl;
        this.isRead = false;
    }

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
