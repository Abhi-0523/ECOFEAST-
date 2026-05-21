package com.ecofeast.model;

import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class ContactMessageTest {

    @Test
    public void testConstructorsAndAccessors() {
        // Parameterized constructor
        ContactMessage msg1 = new ContactMessage("Jane Doe", "jane@example.com", "Question", "How does redistribution work?");
        assertEquals("Jane Doe", msg1.getSenderName());
        assertEquals("jane@example.com", msg1.getSenderEmail());
        assertEquals("Question", msg1.getSubject());
        assertEquals("How does redistribution work?", msg1.getMessage());
        assertFalse(msg1.isReplied());

        // Default constructor and accessors
        ContactMessage msg2 = new ContactMessage();
        msg2.setMessageId(101);
        msg2.setSenderName("Bob");
        msg2.setSenderEmail("bob@example.com");
        msg2.setSubject("Bug Report");
        msg2.setMessage("Page not loading");
        msg2.setReplied(true);
        msg2.setAdminReply("We fixed the issue.");
        
        LocalDateTime replied = LocalDateTime.now().minusMinutes(30);
        LocalDateTime created = LocalDateTime.now().minusHours(1);
        msg2.setRepliedAt(replied);
        msg2.setCreatedAt(created);

        assertEquals(101, msg2.getMessageId());
        assertEquals("Bob", msg2.getSenderName());
        assertEquals("bob@example.com", msg2.getSenderEmail());
        assertEquals("Bug Report", msg2.getSubject());
        assertEquals("Page not loading", msg2.getMessage());
        assertTrue(msg2.isReplied());
        assertEquals("We fixed the issue.", msg2.getAdminReply());
        assertEquals(replied, msg2.getRepliedAt());
        assertEquals(created, msg2.getCreatedAt());
    }
}
