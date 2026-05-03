package com.ecofeast.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * FoodItem Model Class
 * Represents a food item available for redistribution in the EcoFeast system.
 */
public class FoodItem {
    private int itemId;
    private int adminId;
    private String itemName;
    private String description;
    private String category;
    private int quantity;
    private LocalDate expiryDate;
    private String location;
    private String imageUrl;
    private String status; // AVAILABLE, RESERVED, DISTRIBUTED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default Constructor
    public FoodItem() {
    }

    // Constructor with all parameters
    public FoodItem(int itemId, int adminId, String itemName, String description,
                    String category, int quantity, LocalDate expiryDate,
                    String location, String imageUrl, String status,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.itemId = itemId;
        this.adminId = adminId;
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.location = location;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor for creating new food item (without itemId and timestamps)
    public FoodItem(int adminId, String itemName, String description,
                    String category, int quantity, LocalDate expiryDate, String location) {
        this.adminId = adminId;
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.location = location;
        this.status = "AVAILABLE";
    }

    // Getters and Setters
    /**
     * Gets the unique item ID.
     * @return the item ID
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Sets the unique item ID.
     * @param itemId the item ID to set
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the admin ID who posted this food item.
     * @return the admin ID
     */
    public int getAdminId() {
        return adminId;
    }

    /**
     * Sets the admin ID.
     * @param adminId the admin ID to set
     */
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    /**
     * Gets the name of the food item.
     * @return the item name
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Sets the name of the food item.
     * @param itemName the item name to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Gets the description of the food item.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the food item.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the category of the food item.
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the food item.
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the quantity of the food item available.
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the food item.
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the expiry date of the food item.
     * @return the expiry date
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date of the food item.
     * @param expiryDate the expiry date to set
     */
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Gets the location from where the food item is available.
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the food item.
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the image URL of the food item.
     * @return the image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL of the food item.
     * @param imageUrl the image URL to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the status of the food item (AVAILABLE, RESERVED, DISTRIBUTED).
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the food item.
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the creation timestamp.
     * @return the created at timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * @param createdAt the created at timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
     * Checks if the food item is still available (not expired).
     * @return true if still available, false otherwise
     */
    public boolean isAvailable() {
        return "AVAILABLE".equals(status) && LocalDate.now().isBefore(expiryDate);
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", expiryDate=" + expiryDate +
                ", status='" + status + '\'' +
                '}';
    }
}
