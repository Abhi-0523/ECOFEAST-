package com.ecofeast.util;

import com.ecofeast.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * SessionUtil - Centralized helper for session management.
 *
 * Provides:
 *  - Storing and retrieving the logged-in user
 *  - Role-checking shortcuts
 *  - Flash-message helpers (success / error across redirects)
 *  - Secure session invalidation on logout
 */
public class SessionUtil {

    /** Session key used to store the logged-in User object. */
    public static final String SESSION_USER = "loggedInUser";

    /** Session key for flash success messages (survive one redirect). */
    public static final String FLASH_SUCCESS = "successMsg";

    /** Session key for flash error messages. */
    public static final String FLASH_ERROR   = "errorMsg";

    // ------------------------------------------------------------------
    // USER STORAGE
    // ------------------------------------------------------------------

    /**
     * Stores the authenticated user in the HTTP session.
     * Called after a successful login.
     *
     * @param request the HTTP request (provides session access)
     * @param user    the authenticated User object
     */
    public static void setLoggedInUser(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_USER, user);
        // Fix session timeout to 30 minutes for security
        session.setMaxInactiveInterval(30 * 60);
    }

    /**
     * Retrieves the currently logged-in User from the session.
     * Returns null if no active session or user not present.
     *
     * @param request the HTTP request
     * @return logged-in User, or null
     */
    public static User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute(SESSION_USER);
    }

    /**
     * Checks whether there is a valid authenticated session.
     *
     * @param request the HTTP request
     * @return true if a user is logged in
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        return getLoggedInUser(request) != null;
    }

    // ------------------------------------------------------------------
    // ROLE HELPERS
    // ------------------------------------------------------------------

    /**
     * Returns true if the logged-in user has the ADMIN role.
     *
     * @param request the HTTP request
     * @return true if admin
     */
    public static boolean isAdmin(HttpServletRequest request) {
        User user = getLoggedInUser(request);
        return user != null && user.getRole() != null && "ADMIN".equals(normalizeRoleName(user.getRole().getRoleName()));
    }

    /**
     * Returns true if the logged-in user has the DONOR role.
     *
     * @param request the HTTP request
     * @return true if donor
     */
    public static boolean isDonor(HttpServletRequest request) {
        User user = getLoggedInUser(request);
        return user != null && user.getRole() != null && "DONOR".equals(normalizeRoleName(user.getRole().getRoleName()));
    }

    /**
     * Returns true if the logged-in user has the NGO role.
     *
     * @param request the HTTP request
     * @return true if NGO
     */
    public static boolean isNgo(HttpServletRequest request) {
        User user = getLoggedInUser(request);
        return user != null && user.getRole() != null && "NGO".equals(normalizeRoleName(user.getRole().getRoleName()));
    }

    /**
     * Returns true if the logged-in user has the VOLUNTEER role.
     *
     * @param request the HTTP request
     * @return true if volunteer
     */
    public static boolean isVolunteer(HttpServletRequest request) {
        User user = getLoggedInUser(request);
        return user != null && user.getRole() != null && "VOLUNTEER".equals(normalizeRoleName(user.getRole().getRoleName()));
    }

    /**
     * Returns the role name string for the logged-in user.
     * Returns null if no user or no role assigned.
     *
     * @param request the HTTP request
     * @return role name string (e.g., "ADMIN", "DONOR") or null
     */
    public static String getUserRole(HttpServletRequest request) {
        User user = getLoggedInUser(request);
        if (user == null || user.getRole() == null) return null;
        String normalized = normalizeRoleName(user.getRole().getRoleName());
        return normalized.isEmpty() ? null : normalized;
    }

    public static String getCurrentRole(HttpServletRequest request) {
        return getUserRole(request);
    }

    // ------------------------------------------------------------------
    // FLASH MESSAGES (survive one redirect)
    // ------------------------------------------------------------------

    /**
     * Stores a success flash message in the session.
     * Should be read and cleared on the next page load.
     *
     * @param request the HTTP request
     * @param message the success message text
     */
    public static void setSuccessMessage(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute(FLASH_SUCCESS, message);
    }

    /**
     * Stores an error flash message in the session.
     *
     * @param request the HTTP request
     * @param message the error message text
     */
    public static void setErrorMessage(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute(FLASH_ERROR, message);
    }

    /**
     * Retrieves and clears the flash success message from the session.
     * Call once per page load to prevent stale messages.
     *
     * @param request the HTTP request
     * @return the flash success message, or null
     */
    public static String consumeSuccessMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        String msg = (String) session.getAttribute(FLASH_SUCCESS);
        session.removeAttribute(FLASH_SUCCESS);
        return msg;
    }

    /**
     * Retrieves and clears the flash error message from the session.
     *
     * @param request the HTTP request
     * @return the flash error message, or null
     */
    public static String consumeErrorMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        String msg = (String) session.getAttribute(FLASH_ERROR);
        session.removeAttribute(FLASH_ERROR);
        return msg;
    }

    // ------------------------------------------------------------------
    // LOGOUT
    // ------------------------------------------------------------------

    /**
     * Invalidates the current session — used during logout.
     * Removes all session attributes and destroys the session cookie.
     *
     * @param request the HTTP request
     */
    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /** Uppercase trimmed role name; empty string if missing (matches AuthFilter RBAC normalization). */
    private static String normalizeRoleName(String roleName) {
        if (roleName == null) return "";
        return roleName.trim().toUpperCase();
    }

    // Private constructor — utility class, not instantiable
    private SessionUtil() { }
}
