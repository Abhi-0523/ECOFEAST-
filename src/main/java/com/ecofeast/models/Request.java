package com.ecofeast.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Request Model Class
 * Represents a user's request to receive a food item in the EcoFeast system.
 */
public class Request {
    private int requestId;
    private int userId;
    private int itemId;
    private LocalDateTime requestDate;
    private int quantityRequested;
    private String status; // PENDING, APPROVED, REJECTED, COLLECTED
    private String rejectionReason;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private LocalDate collectionDate;
    private LocalDateTime updatedAt;

    // Default Constructor
    public Request() {
    }

    // Constructor with all parameters
    public Request(int requestId, int userId, int itemId, LocalDateTime requestDate,
                   int quantityRequested, String status, String rejectionReason,
                   LocalDate issueDate, LocalDate returnDate, LocalDate collectionDate, LocalDateTime updatedAt) {
        this.requestId = requestId;
        this.userId = userId;
        this.itemId = itemId;
        this.requestDate = requestDate;
        this.quantityRequested = quantityRequested;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.collectionDate = collectionDate;
        this.updatedAt = updatedAt;
    }

    // Constructor for creating new request (without requestId and timestamps)
    public Request(int userId, int itemId, int quantityRequested) {
        this.userId = userId;
        this.itemId = itemId;
        this.quantityRequested = quantityRequested;
        this.status = "PENDING";
    }

    // Getters and Setters
    /**
     * Gets the unique request ID.
     * @return the request ID
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Sets the unique request ID.
     * @param requestId the request ID to set
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the user ID of the requester.
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID of the requester.
     * @param userId the user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the food item ID being requested.
     * @return the item ID
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Sets the food item ID being requested.
     * @param itemId the item ID to set
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the timestamp when the request was made.
     * @return the request date
     */
    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the timestamp when the request was made.
     * @param requestDate the request date to set
     */
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * Gets the quantity of the food item requested.
     * @return the quantity requested
     */
    public int getQuantityRequested() {
        return quantityRequested;
    }

    /**
     * Sets the quantity of the food item requested.
     * @param quantityRequested the quantity requested to set
     */
    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    /**
     * Gets the status of the request (PENDING, APPROVED, REJECTED, COLLECTED).
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the request.
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the rejection reason if the request was rejected.
     * @return the rejection reason
     */
    public String getRejectionReason() {
        return rejectionReason;
    }

    /**
     * Sets the rejection reason.
     * @param rejectionReason the rejection reason to set
     */
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    /**
     * Gets the date the item was issued.
     * @return the issue date
     */
    public LocalDate getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the date the item was issued.
     * @param issueDate the issue date to set
     */
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * Gets the expected return date.
     * @return the return date
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Sets the expected return date.
     * @param returnDate the return date to set
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Gets the date when the food item was collected.
     * @return the collection date
     */
    public LocalDate getCollectionDate() {
        return collectionDate;
    }

    /**
     * Sets the date when the food item was collected.
     * @param collectionDate the collection date to set
     */
    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }

    /**
     * Gets the last update timestamp.
     * @return the updated at timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     * @param updatedAt the updated at timestamp to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Checks if the request is still pending.
     * @return true if status is PENDING, false otherwise
     */
    public boolean isPending() {
        return "PENDING".equals(status);
    }

    /**
     * Checks if the request has been approved.
     * @return true if status is APPROVED, false otherwise
     */
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", quantityRequested=" + quantityRequested +
                ", status='" + status + '\'' +
                '}';
    }
}
