package com.ecofeast.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Password hashing and verification for EcoFeast.
 * Primary format: MD5 hex (32 chars), matching {@code database/schema_full.sql} seeds.
 * Also accepts SHA-256 hex (64 chars) or legacy plaintext for older/demo databases.
 */
public class PasswordUtil {

    public static String hashPassword(String password) {
        return digestHex("MD5", password);
    }

    private static String digestHex(String algorithm, String password) {
        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuilder hex = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(algorithm + " not available: " + e.getMessage());
        }
    }

    /**
     * Verifies plain text against stored value: MD5 hex, SHA-256 hex, or legacy plaintext.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        String stored = hashedPassword.trim();
        if (stored.isEmpty()) {
            return false;
        }
        String plain = plainPassword.trim();

        if (isHexString(stored, 32)) {
            return digestHex("MD5", plain).equalsIgnoreCase(stored);
        }
        if (isHexString(stored, 64)) {
            return digestHex("SHA-256", plain).equalsIgnoreCase(stored);
        }
        // Legacy / mis-seeded databases (plaintext in password_hash column)
        return plain.equals(stored);
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return verifyPassword(plainPassword, hashedPassword);
    }

    private static boolean isHexString(String s, int length) {
        if (s.length() != length) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c < '0' || c > '9') && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
}
