package com.ecofeast.config;

/**
 * Application configuration constants for EcoFeast.
 */
public final class AppConfig {
    private AppConfig() { }

    public static final String DB_URL = "jdbc:mysql://localhost:3306/ecofeast?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    public static final String DB_USER = "root";
    /** Change to match your local MySQL root password. */
    public static final String DB_PASSWORD = "1234";
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    public static final String[] PUBLIC_PATHS = {"/login", "/register", "/index.jsp", "/about.jsp", "/contact.jsp", "/faq.jsp", "/css/", "/js/", "/images/"};

    /**
     * Default administrator created by {@code database/schema_full.sql}.
     * Shown on the login page so users do not confuse a personal email with the seeded admin account.
     */
    public static final String DEMO_ADMIN_EMAIL = "admin@ecofeast.com";
    public static final String DEMO_ADMIN_PASSWORD = "admin123";
}
