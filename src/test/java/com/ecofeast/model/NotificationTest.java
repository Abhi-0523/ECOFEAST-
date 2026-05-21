package com.ecofeast.model;

import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class NotificationTest {

    @Test
    public void testConstructorsAndAccessors() {
        // Parameterized constructor
        Notification notif1 = new Notification(
            15, "New Donation", "A new donation is available in your city.", 
            "DONATION", "/donor/dashboard"
        );

        assertEquals(15, notif1.getUserId());
        assertEquals("New Donation", notif1.getTitle());
        assertEquals("A new donation is available in your city.", notif1.getMessage());
        assertEquals("DONATION", notif1.getType());
        assertEquals("/donor/dashboard", notif1.getLinkUrl());
        assertFalse(notif1.isRead());

        // Default constructor and accessors
        Notification notif2 = new Notification();
        notif2.setNotificationId(999);
        notif2.setUserId(22);
        notif2.setTitle("Alert");
        notif2.setMessage("Task accepted");
        notif2.setType("TASK");
        notif2.setRead(true);
        notif2.setLinkUrl("/volunteer/dashboard");
        LocalDateTime created = LocalDateTime.now();
        notif2.setCreatedAt(created);

        assertEquals(999, notif2.getNotificationId());
        assertEquals(22, notif2.getUserId());
        assertEquals("Alert", notif2.getTitle());
        assertEquals("Task accepted", notif2.getMessage());
        assertEquals("TASK", notif2.getType());
        assertTrue(notif2.isRead());
        assertEquals("/volunteer/dashboard", notif2.getLinkUrl());
        assertEquals(created, notif2.getCreatedAt());
    }
}
