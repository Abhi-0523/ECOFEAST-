package com.ecofeast.model;

import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class DonationRequestTest {

    @Test
    public void testConstructorsAndAccessors() {
        LocalDateTime requested = LocalDateTime.now().minusDays(1);
        LocalDateTime responded = LocalDateTime.now().minusHours(5);
        LocalDateTime collected = LocalDateTime.now();

        // Test parameterized constructor
        DonationRequest req1 = new DonationRequest(201, 301, 5, "Requesting for food distribution campaign");
        assertEquals(201, req1.getDonationId());
        assertEquals(301, req1.getNgoId());
        assertEquals(5, req1.getQuantityRequested());
        assertEquals("Requesting for food distribution campaign", req1.getRequestMessage());

        // Test default constructor and accessors
        DonationRequest req2 = new DonationRequest();
        req2.setRequestId(10);
        req2.setDonationId(100);
        req2.setNgoId(200);
        req2.setQuantityRequested(2);
        req2.setRequestMessage("Please approve");
        req2.setStatus("APPROVED");
        req2.setRejectionReason("Not enough quantity");
        req2.setRequestedAt(requested);
        req2.setRespondedAt(responded);
        req2.setCollectedAt(collected);
        req2.setNgoName("Feeding America");
        req2.setFoodName("Pancakes");
        req2.setDonorName("Donor ABC");

        assertEquals(10, req2.getRequestId());
        assertEquals(100, req2.getDonationId());
        assertEquals(200, req2.getNgoId());
        assertEquals(2, req2.getQuantityRequested());
        assertEquals("Please approve", req2.getRequestMessage());
        assertEquals("APPROVED", req2.getStatus());
        assertEquals("Not enough quantity", req2.getRejectionReason());
        assertEquals(requested, req2.getRequestedAt());
        assertEquals(responded, req2.getRespondedAt());
        assertEquals(collected, req2.getCollectedAt());
        assertEquals("Feeding America", req2.getNgoName());
        assertEquals("Pancakes", req2.getFoodName());
        assertEquals("Donor ABC", req2.getDonorName());
    }
}
