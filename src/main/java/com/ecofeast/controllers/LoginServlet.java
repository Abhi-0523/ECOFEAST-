package com.ecofeast.controllers;

import com.ecofeast.models.User;
import com.ecofeast.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.ecofeast.service.UserService;

public class LoginServlet extends HttpServlet {

    /**
     * Handles GET requests - displays the login form.
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to login view
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    /**
     * Handles POST requests - processes login form submission.
     * Validates credentials and creates a session for authenticated users.
     * @param request the servlet request containing email and password
     * @param response the servlet response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get email and password from request
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            // Validate input
            if (!ValidationUtil.isNotEmpty(email) || !ValidationUtil.isNotEmpty(password)) {
                request.setAttribute("error", "Email and password are required");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }
            
            // Validate email format
            if (!ValidationUtil.isValidEmail(email)) {
                request.setAttribute("error", "Invalid email format");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }
            
            // Authenticate user
            User user = authenticateUser(email, password);
            
            if (user != null && user.isActive()) {
                if ("PENDING".equals(user.getAccountStatus())) {
                    request.setAttribute("error", "Your account is pending admin approval. Please try again later.");
                    request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                    return;
                } else if ("REJECTED".equals(user.getAccountStatus())) {
                    request.setAttribute("error", "Your account registration was rejected.");
                    request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                    return;
                }

                // Authentication successful - create session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userRole", user.getRole());
                session.setAttribute("userName", user.getFullName());
                
                // Redirect based on user role
                if ("ADMIN".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/user/dashboard");
                }
            } else {
                // Authentication failed
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Log error and display error message
            System.err.println("Login error: " + e.getMessage());
            request.setAttribute("error", "An error occurred during login. Please try again.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    /**
     * Authenticates a user by checking email and password in the database.
     * @param email the user's email address
     * @param password the user's password (plain text)
     */
    private User authenticateUser(String email, String password) throws Exception {
        return UserService.authenticateUser(email, password);
    }
}
