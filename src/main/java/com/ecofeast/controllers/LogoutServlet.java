package com.ecofeast.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LogoutServlet - Handles user logout requests.
 * Invalidates the user session and redirects to login page.
 */
public class LogoutServlet extends HttpServlet {

    /**
     * Handles GET requests - logs out the user.
     * Invalidates the session and redirects to login page.
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Invalidate the session
            session.invalidate();
        }
        
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/views/login.jsp");
    }
}
