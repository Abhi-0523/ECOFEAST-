package com.ecofeast.util;

import java.util.regex.Pattern;

/**
 * ValidationUtil - Centralized input validation utility class.
 *
 * All methods are static — no instantiation needed.
 * Used by Servlet Controllers before passing data to the Service layer.
 *
 * Provides:
 *  - Null / empty checks
 *  - Email format validation
 *  - Phone number format validation
 *  - Password strength validation
 *  - Quantity range validation
 *  - String length validation
 *  - XSS / HTML injection prevention (basic sanitization)
 */
public class ValidationUtil {

    // Compiled regex patterns for performance
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[+]?[0-9]{10,15}$");

    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    // Private constructor — utility class
    private ValidationUtil() { }

    // ------------------------------------------------------------------
    // NULL / EMPTY CHECKS
    // ------------------------------------------------------------------

    /**
     * Returns true if the given string is non-null and not blank.
     * Handles whitespace-only strings.
     *
     * @param value the string to check
     * @return true if non-empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * Returns true if the string length is within the allowed maximum.
     *
     * @param value     the string to check
     * @param maxLength maximum allowed character count
     * @return true if within bounds
     */
    public static boolean isWithinLength(String value, int maxLength) {
        return value != null && value.length() <= maxLength;
    }

    /**
     * Returns true if the string length is within min and max bounds.
     *
     * @param value     the string to check
     * @param minLength minimum required character count
     * @param maxLength maximum allowed character count
     * @return true if within bounds
     */
    public static boolean isWithinLength(String value, int minLength, int maxLength) {
        return value != null && value.length() >= minLength && value.length() <= maxLength;
    }

    // ------------------------------------------------------------------
    // FORMAT VALIDATION
    // ------------------------------------------------------------------

    /**
     * Validates email address format using RFC-like regex.
     *
     * @param email the email to validate
     * @return true if email is valid
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates phone number format (international or local, 10-15 digits).
     *
     * @param phone the phone number to validate
     * @return true if phone is valid
     */
    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validates password strength.
     * Requires: min 8 chars, at least one uppercase, one lowercase, one digit.
     *
     * @param password the raw password to validate
     * @return true if password meets strength requirements
     */
    public static boolean isStrongPassword(String password) {
        if (!isNotEmpty(password)) return false;
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Validates that two password strings match.
     *
     * @param password        the original password
     * @param confirmPassword the confirmation password
     * @return true if both match
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    // ------------------------------------------------------------------
    // NUMERIC VALIDATION
    // ------------------------------------------------------------------

    /**
     * Returns true if the given quantity is greater than zero.
     *
     * @param quantity the quantity to validate
     * @return true if positive
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    /**
     * Safely parses a string to an integer.
     * Returns -1 if parsing fails.
     *
     * @param value the string to parse
     * @return parsed integer or -1
     */
    public static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ------------------------------------------------------------------
    // SANITIZATION — Basic XSS Prevention
    // ------------------------------------------------------------------

    /**
     * Strips HTML tags and encodes special characters to prevent XSS.
     * Should be applied to any user-supplied text before storage or display.
     *
     * @param input the raw user input
     * @return sanitized string
     */
    public static String sanitize(String input) {
        if (input == null) return null;
        return input.trim()
                    .replace("&",  "&amp;")
                    .replace("<",  "&lt;")
                    .replace(">",  "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'",  "&#x27;");
    }

    /**
     * Sanitizes input and truncates to a maximum length.
     * Convenience method for safe form field handling.
     *
     * @param input     the raw input string
     * @param maxLength maximum allowed length after sanitization
     * @return sanitized and truncated string
     */
    public static String sanitizeAndTrim(String input, int maxLength) {
        String sanitized = sanitize(input);
        if (sanitized == null) return null;
        return sanitized.length() > maxLength ? sanitized.substring(0, maxLength) : sanitized;
    }
}
