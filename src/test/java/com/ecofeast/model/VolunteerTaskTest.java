package com.ecofeast.model;

import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class VolunteerTaskTest {

    @Test
    public void testGettersAndSetters() {
        VolunteerTask task = new VolunteerTask();
        task.setTaskId(50);
        task.setRequestId(100);
        task.setVolunteerId(200);
        task.setTaskType("DELIVERY");
        task.setPickupAddress("123 Food Bank St");
        task.setDeliveryAddress("456 Hope Center Rd");

        LocalDateTime scheduled = LocalDateTime.now().plusHours(4);
        LocalDateTime accepted = LocalDateTime.now().plusMinutes(10);
        LocalDateTime completed = LocalDateTime.now().plusHours(2);
        LocalDateTime created = LocalDateTime.now().minusHours(1);
        LocalDateTime updated = LocalDateTime.now();

        task.setScheduledTime(scheduled);
        task.setAcceptedAt(accepted);
        task.setCompletedAt(completed);
        task.setStatus("COMPLETED");
        task.setNotes("Delivered successfully without issues.");
        task.setCreatedAt(created);
        task.setUpdatedAt(updated);

        task.setFoodName("Apple Pie");
        task.setNgoName("Hope Org");
        task.setDonorName("Pie Factory");
        task.setVolunteerName("Jane Volunteer");

        assertEquals(50, task.getTaskId());
        assertEquals(100, task.getRequestId());
        assertEquals(Integer.valueOf(200), task.getVolunteerId());
        assertEquals("DELIVERY", task.getTaskType());
        assertEquals("123 Food Bank St", task.getPickupAddress());
        assertEquals("456 Hope Center Rd", task.getDeliveryAddress());
        assertEquals(scheduled, task.getScheduledTime());
        assertEquals(accepted, task.getAcceptedAt());
        assertEquals(completed, task.getCompletedAt());
        assertEquals("COMPLETED", task.getStatus());
        assertEquals("Delivered successfully without issues.", task.getNotes());
        assertEquals(created, task.getCreatedAt());
        assertEquals(updated, task.getUpdatedAt());
        assertEquals("Apple Pie", task.getFoodName());
        assertEquals("Hope Org", task.getNgoName());
        assertEquals("Pie Factory", task.getDonorName());
        assertEquals("Jane Volunteer", task.getVolunteerName());
    }
}
