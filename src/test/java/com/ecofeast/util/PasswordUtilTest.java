package com.ecofeast.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordUtilTest {

    @Test
    public void testHashPassword() {
        String plain = "mySecretPassword123";
        String hash1 = PasswordUtil.hashPassword(plain);
        String hash2 = PasswordUtil.hashPassword(plain);

        assertNotNull(hash1);
        assertEquals(32, hash1.length()); // MD5 yields 32 hex chars
        assertEquals(hash1, hash2); // Hashing should be deterministic in this implementation
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashPasswordNull() {
        PasswordUtil.hashPassword(null);
    }

    @Test
    public void testVerifyPasswordMD5() {
        String plain = "Password123";
        String hash = PasswordUtil.hashPassword(plain);

        assertTrue(PasswordUtil.verifyPassword(plain, hash));
        assertTrue(PasswordUtil.checkPassword(plain, hash)); // checkPassword is alias for verifyPassword
        assertFalse(PasswordUtil.verifyPassword("WrongPassword", hash));
    }

    @Test
    public void testVerifyPasswordSHA256() throws Exception {
        String plain = "Password123";
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        md.update(plain.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        StringBuilder hex = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            String h = Integer.toHexString(0xff & b);
            if (h.length() == 1) hex.append('0');
            hex.append(h);
        }
        String sha256Hash = hex.toString();

        assertTrue(PasswordUtil.verifyPassword(plain, sha256Hash));
        assertFalse(PasswordUtil.verifyPassword("WrongPassword", sha256Hash));
    }

    @Test
    public void testVerifyPasswordLegacyPlaintext() {
        // Stored password that is neither 32 nor 64 hex chars is treated as legacy plaintext
        String plaintextStored = "plain_pwd_123";
        assertTrue(PasswordUtil.verifyPassword("plain_pwd_123", plaintextStored));
        assertFalse(PasswordUtil.verifyPassword("wrong_pwd", plaintextStored));
    }

    @Test
    public void testVerifyPasswordNullOrEmpty() {
        assertFalse(PasswordUtil.verifyPassword(null, "hash"));
        assertFalse(PasswordUtil.verifyPassword("password", null));
        assertFalse(PasswordUtil.verifyPassword("password", "   "));
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(PasswordUtil.isValidPassword("12345678")); // length 8
        assertTrue(PasswordUtil.isValidPassword("strongPassword")); 
        assertFalse(PasswordUtil.isValidPassword("1234567")); // length 7
        assertFalse(PasswordUtil.isValidPassword(null));
    }
}
