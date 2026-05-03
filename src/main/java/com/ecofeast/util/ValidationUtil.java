package com.ecofeast.util;

/**
 * ValidationUtil - Utility class for input validation.
 * Provides methods to validate user input and prevent common issues.
 */
public class ValidationUtil {

    /**
     * Validates email format using a simple regex pattern.
     * @param email the email address to validate
     * @return true if email format is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Simple email validation regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validates phone number format (allows 10-15 digits).
     * @param phoneNumber the phone number to validate
     * @return true if phone number format is valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        // Allow only digits, with length between 10-15
        return phoneNumber.matches("\\d{10,15}");
    }

    /**
     * Validates that a string is not empty or null.
     * @param value the string to validate
     * @return true if string is not empty, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Checks if a quantity is positive.
     * @param quantity the quantity to validate
     * @return true if quantity is greater than 0, false otherwise
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    /**
     * Sanitizes user input to prevent SQL injection and XSS attacks.
     * Removes special characters and scripts.
     * @param input the user input to sanitize
     * @return the sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // Remove HTML and script tags
        input = input.replaceAll("<[^>]*>", "");
        input = input.replaceAll("'", "''"); // Escape single quotes for SQL
        return input;
    }
}
