package com.ecofeast.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationUtilTest {

    @Test
    public void testIsNotEmpty() {
        assertTrue(ValidationUtil.isNotEmpty("hello"));
        assertTrue(ValidationUtil.isNotEmpty("a"));
        assertFalse(ValidationUtil.isNotEmpty(null));
        assertFalse(ValidationUtil.isNotEmpty(""));
        assertFalse(ValidationUtil.isNotEmpty("   ")); // Handles whitespace-only
    }

    @Test
    public void testIsWithinLengthMaxOnly() {
        assertTrue(ValidationUtil.isWithinLength("abc", 5));
        assertTrue(ValidationUtil.isWithinLength("abcde", 5));
        assertFalse(ValidationUtil.isWithinLength("abcdef", 5));
        assertFalse(ValidationUtil.isWithinLength(null, 5));
    }

    @Test
    public void testIsWithinLengthMinMax() {
        assertTrue(ValidationUtil.isWithinLength("abcd", 3, 5));
        assertTrue(ValidationUtil.isWithinLength("abc", 3, 5));
        assertTrue(ValidationUtil.isWithinLength("abcde", 3, 5));
        assertFalse(ValidationUtil.isWithinLength("ab", 3, 5));
        assertFalse(ValidationUtil.isWithinLength("abcdef", 3, 5));
        assertFalse(ValidationUtil.isWithinLength(null, 3, 5));
    }

    @Test
    public void testIsValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("user.name+tag@domain.co.uk"));
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail("plainaddress"));
        assertFalse(ValidationUtil.isValidEmail("missing@domain")); // missing tld suffix (e.g. .com)
        assertFalse(ValidationUtil.isValidEmail("missing@.com"));
    }

    @Test
    public void testIsValidPhone() {
        assertTrue(ValidationUtil.isValidPhone("1234567890"));
        assertTrue(ValidationUtil.isValidPhone("+123456789012"));
        assertFalse(ValidationUtil.isValidPhone(null));
        assertFalse(ValidationUtil.isValidPhone(""));
        assertFalse(ValidationUtil.isValidPhone("123-456-7890")); // no dash allowed by pattern
        assertFalse(ValidationUtil.isValidPhone("123456789")); // too short (9 digits)
        assertFalse(ValidationUtil.isValidPhone("1234567890123456")); // too long (16 digits)
    }

    @Test
    public void testIsStrongPassword() {
        assertTrue(ValidationUtil.isStrongPassword("Pass1234")); // uppercase, lowercase, digit, length 8
        assertTrue(ValidationUtil.isStrongPassword("AveryStr0ngP@ss"));
        assertFalse(ValidationUtil.isStrongPassword(null));
        assertFalse(ValidationUtil.isStrongPassword(""));
        assertFalse(ValidationUtil.isStrongPassword("short1A")); // length 7
        assertFalse(ValidationUtil.isStrongPassword("nouppercase1"));
        assertFalse(ValidationUtil.isStrongPassword("NOLOWERCASE1"));
        assertFalse(ValidationUtil.isStrongPassword("NoDigitsLetters"));
    }

    @Test
    public void testPasswordsMatch() {
        assertTrue(ValidationUtil.passwordsMatch("pass", "pass"));
        assertFalse(ValidationUtil.passwordsMatch("pass", "other"));
        assertFalse(ValidationUtil.passwordsMatch(null, "pass"));
        assertFalse(ValidationUtil.passwordsMatch("pass", null));
    }

    @Test
    public void testIsValidQuantity() {
        assertTrue(ValidationUtil.isValidQuantity(1));
        assertTrue(ValidationUtil.isValidQuantity(999));
        assertFalse(ValidationUtil.isValidQuantity(0));
        assertFalse(ValidationUtil.isValidQuantity(-5));
    }

    @Test
    public void testParseIntSafe() {
        assertEquals(123, ValidationUtil.parseIntSafe("123"));
        assertEquals(0, ValidationUtil.parseIntSafe("0"));
        assertEquals(-5, ValidationUtil.parseIntSafe("-5"));
        assertEquals(-1, ValidationUtil.parseIntSafe("abc"));
        assertEquals(-1, ValidationUtil.parseIntSafe(""));
        assertEquals(-1, ValidationUtil.parseIntSafe(null));
    }

    @Test
    public void testSanitize() {
        assertNull(ValidationUtil.sanitize(null));
        assertEquals("hello", ValidationUtil.sanitize("  hello  ")); // trims
        assertEquals("&lt;script&gt;alert(&#x27;XSS&#x27;)&lt;/script&gt;", 
                     ValidationUtil.sanitize("<script>alert('XSS')</script>"));
        assertEquals("&quot;test&quot; &amp; &quot;demo&quot;", 
                     ValidationUtil.sanitize("\"test\" & \"demo\""));
    }

    @Test
    public void testSanitizeAndTrim() {
        assertNull(ValidationUtil.sanitizeAndTrim(null, 5));
        assertEquals("abcde", ValidationUtil.sanitizeAndTrim("abcdefgh", 5));
        assertEquals("hello", ValidationUtil.sanitizeAndTrim("hello", 10));
        assertEquals("&lt;s", ValidationUtil.sanitizeAndTrim("<script>", 5));
    }
}
