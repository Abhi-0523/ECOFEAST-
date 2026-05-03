package com.ecofeast.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordUtil - Utility class for password hashing and validation.
 * Uses MD5 hashing for password encryption (suitable for learning purposes).
 * Note: In production, use bcrypt, PBKDF2, or Argon2.
 */
public class PasswordUtil {

    /**
     * Hashes a plain text password using MD5 algorithm.
     * @param password the plain text password to hash
     * @return the MD5 hashed password
     */
    public static String hashPassword(String password) {
        try {
            // Create MD5 message digest
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // Update with password bytes
            md.update(password.getBytes(StandardCharsets.UTF_8));
            
            // Get digest and convert to hex string
            byte[] messageDigest = md.digest();
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // MD5 algorithm not available
            throw new RuntimeException("MD5 algorithm not available: " + e.getMessage());
        }
    }

    /**
     * Verifies a plain text password against a hashed password.
     * @param plainPassword the plain text password to verify
     * @param hashedPassword the hashed password to compare against
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedInput = hashPassword(plainPassword);
        return hashedInput.equals(hashedPassword);
    }

    /**
     * Validates password strength.
     * Password must be at least 8 characters long.
     * @param password the password to validate
     * @return true if password meets minimum requirements, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
}
