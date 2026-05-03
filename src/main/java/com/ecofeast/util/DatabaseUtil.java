package com.ecofeast.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseUtil - Utility class for database connections.
 * Provides a centralized way to manage MySQL database connections using JDBC.
 */
public class DatabaseUtil {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecofeast";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "your password here";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Gets a database connection to the EcoFeast MySQL database.
     * Ensures the JDBC driver is loaded before creating the connection.
     * @return a Connection object to the database
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            // Driver loading failed - throw SQL exception
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage());
        }

        try {
            // Attempt to establish a connection to the database
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            // Connection failed - provide detailed error message
            throw new SQLException("Failed to connect to database: " + e.getMessage());
        }
    }

    /**
     * Tests the database connection.
     * Useful for verifying that the database is accessible.
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
        }
        return false;
    }
}
