package com.ecofeast.model;

import java.time.LocalDateTime;

public class ContactMessage {
    private int messageId;
    private String senderName;
    private String senderEmail;
    private String subject;
    private String message;
    private boolean isReplied;
    private String adminReply;
    private LocalDateTime repliedAt;
    private LocalDateTime createdAt;

    public ContactMessage() {}

    public ContactMessage(String senderName, String senderEmail, String subject, String message) {
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.subject = subject;
        this.message = message;
    }

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isReplied() { return isReplied; }
    public void setReplied(boolean replied) { isReplied = replied; }
    public String getAdminReply() { return adminReply; }
    public void setAdminReply(String adminReply) { this.adminReply = adminReply; }
    public LocalDateTime getRepliedAt() { return repliedAt; }
    public void setRepliedAt(LocalDateTime repliedAt) { this.repliedAt = repliedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
