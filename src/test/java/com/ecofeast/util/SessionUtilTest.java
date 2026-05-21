package com.ecofeast.util;

import com.ecofeast.model.Role;
import com.ecofeast.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SessionUtilTest {

    private HttpServletRequest request;
    private HttpSession session;
    private User testUser;
    private Role adminRole;
    private Role donorRole;
    private Role ngoRole;
    private Role volunteerRole;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        
        testUser = new User();
        testUser.setId(42);
        testUser.setEmail("test@ecofeast.com");
        testUser.setFullName("Test User");

        adminRole = new Role(1, "ADMIN");
        donorRole = new Role(2, "DONOR");
        ngoRole = new Role(3, "NGO");
        volunteerRole = new Role(4, "VOLUNTEER");
    }

    @Test
    public void testSetLoggedInUser() {
        when(request.getSession(true)).thenReturn(session);

        SessionUtil.setLoggedInUser(request, testUser);

        verify(session).setAttribute(SessionUtil.SESSION_USER, testUser);
        verify(session).setMaxInactiveInterval(30 * 60);
    }

    @Test
    public void testGetLoggedInUserNoSession() {
        when(request.getSession(false)).thenReturn(null);
        assertNull(SessionUtil.getLoggedInUser(request));
    }

    @Test
    public void testGetLoggedInUserNoUserAttribute() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(null);
        
        assertNull(SessionUtil.getLoggedInUser(request));
    }

    @Test
    public void testGetLoggedInUserSuccess() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(testUser);

        User retrieved = SessionUtil.getLoggedInUser(request);
        assertNotNull(retrieved);
        assertEquals(testUser.getId(), retrieved.getId());
    }

    @Test
    public void testIsLoggedIn() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(testUser);
        assertTrue(SessionUtil.isLoggedIn(request));

        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(null);
        assertFalse(SessionUtil.isLoggedIn(request));
    }

    @Test
    public void testIsAdmin() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(testUser);

        // No role set
        assertFalse(SessionUtil.isAdmin(request));

        // Admin role set
        testUser.setRole(adminRole);
        assertTrue(SessionUtil.isAdmin(request));

        // Donor role set
        testUser.setRole(donorRole);
        assertFalse(SessionUtil.isAdmin(request));
    }

    @Test
    public void testIsDonor() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(testUser);

        // Donor role
        testUser.setRole(donorRole);
        assertTrue(SessionUtil.isDonor(request));

        // Admin role
        testUser.setRole(adminRole);
        assertFalse(SessionUtil.isDonor(request));
    }

    @Test
    public void testIsNgo() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(testUser);

        // NGO role
        testUser.setRole(ngoRole);
        assertTrue(SessionUtil.isNgo(request));

        // Admin role
        testUser.setRole(adminRole);
        assertFalse(SessionUtil.isNgo(request));
    }

    @Test
    public void testIsVolunteer() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(testUser);

        // Volunteer role
        testUser.setRole(volunteerRole);
        assertTrue(SessionUtil.isVolunteer(request));

        // NGO role
        testUser.setRole(ngoRole);
        assertFalse(SessionUtil.isVolunteer(request));
    }

    @Test
    public void testGetUserRoleAndGetCurrentRole() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.SESSION_USER)).thenReturn(testUser);

        // No role
        assertNull(SessionUtil.getUserRole(request));

        // With role
        testUser.setRole(ngoRole);
        assertEquals("NGO", SessionUtil.getUserRole(request));
        assertEquals("NGO", SessionUtil.getCurrentRole(request));
    }

    @Test
    public void testSuccessFlashMessages() {
        when(request.getSession(true)).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.FLASH_SUCCESS)).thenReturn("Task created!");

        // Set
        SessionUtil.setSuccessMessage(request, "Task created!");
        verify(session).setAttribute(SessionUtil.FLASH_SUCCESS, "Task created!");

        // Consume
        String msg = SessionUtil.consumeSuccessMessage(request);
        assertEquals("Task created!", msg);
        verify(session).removeAttribute(SessionUtil.FLASH_SUCCESS);
    }

    @Test
    public void testErrorFlashMessages() {
        when(request.getSession(true)).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.FLASH_ERROR)).thenReturn("Invalid data!");

        // Set
        SessionUtil.setErrorMessage(request, "Invalid data!");
        verify(session).setAttribute(SessionUtil.FLASH_ERROR, "Invalid data!");

        // Consume
        String msg = SessionUtil.consumeErrorMessage(request);
        assertEquals("Invalid data!", msg);
        verify(session).removeAttribute(SessionUtil.FLASH_ERROR);
    }

    @Test
    public void testInvalidateSession() {
        when(request.getSession(false)).thenReturn(session);

        SessionUtil.invalidateSession(request);

        verify(session).invalidate();
    }

    @Test
    public void testInvalidateSessionNoSession() {
        when(request.getSession(false)).thenReturn(null);

        SessionUtil.invalidateSession(request); // should not crash
    }
}
