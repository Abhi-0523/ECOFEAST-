package com.ecofeast.model;

import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class FoodDonationTest {

    @Test
    public void testConstructorsAndGettersSetters() {
        LocalDateTime expiry = LocalDateTime.now().plusDays(2);
        
        // Parameterized constructor 1
        FoodDonation donation1 = new FoodDonation(
            5, 2, "Fresh Apples", "10kg of red apples", 
            10, "KG", expiry, "Downtown Hub", "New York", "apples.jpg"
        );

        assertEquals(5, donation1.getDonorId());
        assertEquals(2, donation1.getCategoryId());
        assertEquals("Fresh Apples", donation1.getFoodName());
        assertEquals("10kg of red apples", donation1.getDescription());
        assertEquals(10, donation1.getQuantity());
        assertEquals("KG", donation1.getQuantityUnit());
        assertEquals(expiry, donation1.getExpiryTime());
        assertEquals("Downtown Hub", donation1.getPickupLocation());
        assertEquals("New York", donation1.getPickupCity());
        assertEquals("apples.jpg", donation1.getImageUrl());

        // Parameterized constructor 2 (without image)
        FoodDonation donation2 = new FoodDonation(
            6, 3, "Bread", "Sliced wheat bread", 
            5, "PACKS", expiry, "Baker St", "London"
        );

        assertEquals(6, donation2.getDonorId());
        assertEquals(3, donation2.getCategoryId());
        assertEquals("Bread", donation2.getFoodName());
        assertEquals("Sliced wheat bread", donation2.getDescription());
        assertEquals(5, donation2.getQuantity());
        assertEquals("PACKS", donation2.getQuantityUnit());
        assertEquals(expiry, donation2.getExpiryTime());
        assertEquals("Baker St", donation2.getPickupLocation());
        assertEquals("London", donation2.getPickupCity());
        assertNull(donation2.getImageUrl());

        // Test status and setters/getters
        FoodDonation donation3 = new FoodDonation();
        donation3.setDonationId(101);
        donation3.setStatus("AVAILABLE");
        donation3.setDonorName("ABC Foods");
        donation3.setCategoryName("Fruits & Vegetables");

        LocalDateTime created = LocalDateTime.now().minusHours(1);
        LocalDateTime updated = LocalDateTime.now();
        donation3.setCreatedAt(created);
        donation3.setUpdatedAt(updated);

        assertEquals(101, donation3.getDonationId());
        assertEquals("AVAILABLE", donation3.getStatus());
        assertTrue(donation3.isAvailable());
        assertEquals("ABC Foods", donation3.getDonorName());
        assertEquals("Fruits & Vegetables", donation3.getCategoryName());
        assertEquals(created, donation3.getCreatedAt());
        assertEquals(updated, donation3.getUpdatedAt());

        donation3.setStatus("COMPLETED");
        assertFalse(donation3.isAvailable());
    }
}
