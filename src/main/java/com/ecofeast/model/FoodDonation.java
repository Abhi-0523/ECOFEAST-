package com.ecofeast.model;

import java.time.LocalDateTime;

public class FoodDonation {
    private int donationId;
    private int donorId;
    private int categoryId;
    private String foodName;
    private String description;
    private int quantity;
    private String quantityUnit;
    private LocalDateTime expiryTime;
    private String pickupLocation;
    private String pickupCity;
    private String imageUrl;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for joined queries
    private String donorName;
    private String categoryName;

    // Default constructor
    public FoodDonation() {}

    public FoodDonation(int donorId, int categoryId, String foodName, String description, int quantity, String quantityUnit, LocalDateTime expiryTime, String pickupLocation, String pickupCity, String imageUrl) {
        this.donorId = donorId;
        this.categoryId = categoryId;
        this.foodName = foodName;
        this.description = description;
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.expiryTime = expiryTime;
        this.pickupLocation = pickupLocation;
        this.pickupCity = pickupCity;
        this.imageUrl = imageUrl;
    }

    public FoodDonation(int donorId, int categoryId, String foodName, String description, int quantity, String quantityUnit, LocalDateTime expiryTime, String pickupLocation, String pickupCity) {
        this(donorId, categoryId, foodName, description, quantity, quantityUnit, expiryTime, pickupLocation, pickupCity, null);
    }

    public boolean isAvailable() {
        return "AVAILABLE".equals(this.status);
    }

    // Getters and Setters
    public int getDonationId() { return donationId; }
    public void setDonationId(int donationId) { this.donationId = donationId; }
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getQuantityUnit() { return quantityUnit; }
    public void setQuantityUnit(String quantityUnit) { this.quantityUnit = quantityUnit; }
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public String getPickupCity() { return pickupCity; }
    public void setPickupCity(String pickupCity) { this.pickupCity = pickupCity; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
