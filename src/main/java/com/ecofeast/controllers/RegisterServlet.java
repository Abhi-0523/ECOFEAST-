package com.ecofeast.controllers;

import com.ecofeast.models.User;
import com.ecofeast.service.UserService;
import com.ecofeast.util.DatabaseUtil;
import com.ecofeast.util.PasswordUtil;
import com.ecofeast.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RegisterServlet - Handles user registration requests.
 * Validates user input, checks for duplicate email/phone numbers, and creates new user accounts.
 * Implements password validation and existence checks as per requirements.
 */
public class RegisterServlet extends HttpServlet {

    /**
     * Handles GET requests - displays the registration form.
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to registration view
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    /**
     * Handles POST requests - processes registration form submission.
     * Validates all input and creates a new user account if validation passes.
     * @param request the servlet request containing registration data
     * @param response the servlet response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get form parameters
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // Validate all required fields are provided
            if (!ValidationUtil.isNotEmpty(firstName) || !ValidationUtil.isNotEmpty(lastName) ||
                !ValidationUtil.isNotEmpty(email) || !ValidationUtil.isNotEmpty(phoneNumber) ||
                !ValidationUtil.isNotEmpty(password) || !ValidationUtil.isNotEmpty(confirmPassword)) {
                
                request.setAttribute("error", "All fields are required");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Validate email format
            if (!ValidationUtil.isValidEmail(email)) {
                request.setAttribute("error", "Invalid email format");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Validate phone number format
            if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
                request.setAttribute("error", "Phone number must be 10-15 digits");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Validate password strength
            if (!PasswordUtil.isValidPassword(password)) {
                request.setAttribute("error", "Password must be at least 8 characters");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Validate passwords match
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Passwords do not match");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Check if email already exists
            if (emailExists(email)) {
                request.setAttribute("error", "Email already registered");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Check if phone number already exists
            if (phoneExists(phoneNumber)) {
                request.setAttribute("error", "Phone number already registered");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Register new user
            if (registerNewUser(firstName, lastName, email, phoneNumber, password)) {
                // Registration successful - redirect to login
                request.setAttribute("success", "Registration successful! Your account is pending admin approval.");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Log error and display error message
            System.err.println("Registration error: " + e.getMessage());
            request.setAttribute("error", "An error occurred during registration. Please try again.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }

    /**
     * Checks if an email already exists in the database.
     * @param email the email address to check
     * @return true if email exists, false otherwise
     * @throws SQLException if database access fails
     */
    private boolean emailExists(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            String sql = "SELECT user_id FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            rs = pstmt.executeQuery();
            return rs.next(); // Returns true if record found
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Checks if a phone number already exists in the database.
     * @param phoneNumber the phone number to check
     * @return true if phone number exists, false otherwise
     * @throws SQLException if database access fails
     */
    private boolean phoneExists(String phoneNumber) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            String sql = "SELECT user_id FROM users WHERE phone_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phoneNumber);
            
            rs = pstmt.executeQuery();
            return rs.next(); // Returns true if record found
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    private boolean registerNewUser(String firstName, String lastName, String email,
                                   String phoneNumber, String password) throws Exception {
        User user = new User(firstName, lastName, email, phoneNumber, password);
        return UserService.registerUser(user);
    }
}
