package com.ecofeast.model;

import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testGettersAndSetters() {
        User user = new User();
        user.setId(10);
        user.setRoleId(3);
        user.setFullName("John Doe");
        user.setEmail("john@example.com");
        user.setPasswordHash("hash123");
        user.setPhone("1234567890");
        user.setOrganization("EcoOrg");
        user.setAddress("123 Green Way");
        user.setCity("EcoCity");
        user.setState("EcoState");
        user.setZipCode("12345");
        user.setProfileImage("avatar.png");
        user.setAccountStatus("APPROVED");
        user.setActive(true);

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        Role role = new Role(3, "NGO");
        user.setRole(role);

        assertEquals(10, user.getId());
        assertEquals(3, user.getRoleId());
        assertEquals("John Doe", user.getFullName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("hash123", user.getPasswordHash());
        assertEquals("1234567890", user.getPhone());
        assertEquals("EcoOrg", user.getOrganization());
        assertEquals("123 Green Way", user.getAddress());
        assertEquals("EcoCity", user.getCity());
        assertEquals("EcoState", user.getState());
        assertEquals("12345", user.getZipCode());
        assertEquals("avatar.png", user.getProfileImage());
        assertEquals("APPROVED", user.getAccountStatus());
        assertTrue(user.isActive());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertNotNull(user.getRole());
        assertEquals("NGO", user.getRole().getRoleName());
    }
}
