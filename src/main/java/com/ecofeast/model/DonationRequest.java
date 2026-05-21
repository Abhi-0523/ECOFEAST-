package com.ecofeast.model;

import java.time.LocalDateTime;

public class DonationRequest {
    private int requestId;
    private int donationId;
    private int ngoId;
    private int quantityRequested;
    private String requestMessage;
    private String status;
    private String rejectionReason;
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
    private LocalDateTime collectedAt;
    
    // Extra fields for views
    private String ngoName;
    private String foodName;
    private String donorName;

    public DonationRequest() {}

    public DonationRequest(int donationId, int ngoId, int quantityRequested, String requestMessage) {
        this.donationId = donationId;
        this.ngoId = ngoId;
        this.quantityRequested = quantityRequested;
        this.requestMessage = requestMessage;
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public int getDonationId() { return donationId; }
    public void setDonationId(int donationId) { this.donationId = donationId; }
    public int getNgoId() { return ngoId; }
    public void setNgoId(int ngoId) { this.ngoId = ngoId; }
    public int getQuantityRequested() { return quantityRequested; }
    public void setQuantityRequested(int quantityRequested) { this.quantityRequested = quantityRequested; }
    public String getRequestMessage() { return requestMessage; }
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }
    public LocalDateTime getCollectedAt() { return collectedAt; }
    public void setCollectedAt(LocalDateTime collectedAt) { this.collectedAt = collectedAt; }
    public String getNgoName() { return ngoName; }
    public void setNgoName(String ngoName) { this.ngoName = ngoName; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }
}
