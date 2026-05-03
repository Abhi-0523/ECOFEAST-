package com.ecofeast.controllers;

import com.ecofeast.models.User;
import com.ecofeast.util.DatabaseUtil;
import com.ecofeast.util.ValidationUtil;

import com.ecofeast.service.AdminService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class AdminController extends HttpServlet {

    /**
     * Handles GET requests for admin operations.
     * Routes requests to appropriate JSP views or operations.
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = getSessionUser(request);
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("dashboard".equals(action)) {
                // Display admin dashboard
                request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
            } else if ("addFood".equals(action)) {
                // Display add food item form
                request.getRequestDispatcher("/views/admin-add-food.jsp").forward(request, response);
            } else if ("manageFoodItems".equals(action)) {
                // Get and display all food items
                manageFoodItems(request, response);
            } else if ("pendingUsers".equals(action)) {
                request.setAttribute("pendingUsers", AdminService.getPendingUsers());
                request.getRequestDispatcher("/views/admin-pending-users.jsp").forward(request, response);
            } else if ("reports".equals(action)) {
                request.setAttribute("reports", AdminService.getReports());
                request.getRequestDispatcher("/views/admin-reports.jsp").forward(request, response);
            } else if ("backupDatabase".equals(action)) {
                response.setContentType("application/sql");
                response.setHeader("Content-Disposition", "attachment; filename=\"ecofeast_backup.sql\"");
                response.getWriter().write("-- EcoFeast Database Backup generated on " + new java.util.Date() + "\n");
                response.getWriter().write("-- (Full automated dump requires configured mysqldump credentials)\n");
                return;
            } else {
                // Default: show dashboard
                request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Admin GET error: " + e.getMessage());
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
        }
    }

    /**
     * Handles POST requests for admin operations.
     * Processes form submissions for creating/updating food items and requests.
     * @param request the servlet request
     * @param response the servlet response
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = getSessionUser(request);
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("addFoodItem".equals(action)) {
                // Add new food item
                addFoodItem(request, response, user);
            } else if ("deleteFoodItem".equals(action)) {
                // Delete food item
                deleteFoodItem(request, response);
            } else if ("approveRequest".equals(action)) {
                // Approve user request
                approveRequest(request, response);
            } else if ("rejectRequest".equals(action)) {
                // Reject user request
                rejectRequest(request, response);
            } else if ("approveUser".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                if (AdminService.approveUser(userId)) {
                    request.setAttribute("success", "User approved successfully!");
                } else {
                    request.setAttribute("error", "Failed to approve user.");
                }
                request.setAttribute("pendingUsers", AdminService.getPendingUsers());
                request.getRequestDispatcher("/views/admin-pending-users.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Admin POST error: " + e.getMessage());
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
        }
    }

    /**
     * Adds a new food item to the database.
     * @param request the servlet request with food item data
     * @param response the servlet response
     * @param user the admin user adding the food item
     * @throws Exception if an error occurs
     */
    private void addFoodItem(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        
        String itemName = request.getParameter("itemName");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String quantityStr = request.getParameter("quantity");
        String expiryDateStr = request.getParameter("expiryDate");
        String location = request.getParameter("location");
        
        // Validate input
        if (!ValidationUtil.isNotEmpty(itemName) || !ValidationUtil.isNotEmpty(category) ||
            !ValidationUtil.isNotEmpty(expiryDateStr) || !ValidationUtil.isNotEmpty(location)) {
            request.setAttribute("error", "All required fields must be filled");
            request.getRequestDispatcher("/views/admin-add-food.jsp").forward(request, response);
            return;
        }
        
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (!ValidationUtil.isValidQuantity(quantity)) {
                request.setAttribute("error", "Quantity must be greater than 0");
                request.getRequestDispatcher("/views/admin-add-food.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid quantity value");
            request.getRequestDispatcher("/views/admin-add-food.jsp").forward(request, response);
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            String sql = "INSERT INTO food_items (admin_id, item_name, description, category, " +
                        "quantity, expiry_date, location, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'AVAILABLE')";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user.getUserId());
            pstmt.setString(2, itemName);
            pstmt.setString(3, description);
            pstmt.setString(4, category);
            pstmt.setInt(5, quantity);
            pstmt.setString(6, expiryDateStr);
            pstmt.setString(7, location);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                request.setAttribute("success", "Food item added successfully!");
                request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to add food item");
                request.getRequestDispatcher("/views/admin-add-food.jsp").forward(request, response);
            }
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Deletes a food item from the database.
     * @param request the servlet request with item ID
     * @param response the servlet response
     * @throws Exception if an error occurs
     */
    private void deleteFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String itemIdStr = request.getParameter("itemId");
        
        if (!ValidationUtil.isNotEmpty(itemIdStr)) {
            request.setAttribute("error", "Item ID is required");
            request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
            return;
        }
        
        int itemId = Integer.parseInt(itemIdStr);
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            String sql = "DELETE FROM food_items WHERE item_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, itemId);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                request.setAttribute("success", "Food item deleted successfully!");
            } else {
                request.setAttribute("error", "Failed to delete food item");
            }
            
            request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Approves a user's food request.
     * @param request the servlet request with request ID
     * @param response the servlet response
     * @throws Exception if an error occurs
     */
    private void approveRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String requestIdStr = request.getParameter("requestId");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            String sql = "UPDATE requests SET status = 'APPROVED' WHERE request_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(requestIdStr));
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                request.setAttribute("success", "Request approved successfully!");
            } else {
                request.setAttribute("error", "Failed to approve request");
            }
            
            request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Rejects a user's food request with optional reason.
     * @param request the servlet request with request ID and reason
     * @param response the servlet response
     * @throws Exception if an error occurs
     */
    private void rejectRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String requestIdStr = request.getParameter("requestId");
        String reason = request.getParameter("reason");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            String sql = "UPDATE requests SET status = 'REJECTED', rejection_reason = ? WHERE request_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reason);
            pstmt.setInt(2, Integer.parseInt(requestIdStr));
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                request.setAttribute("success", "Request rejected successfully!");
            } else {
                request.setAttribute("error", "Failed to reject request");
            }
            
            request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Retrieves and displays all food items.
     * @param request the servlet request
     * @param response the servlet response
     * @throws Exception if an error occurs
     */
    private void manageFoodItems(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            String sql = "SELECT * FROM food_items ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            request.setAttribute("foodItems", rs);
            request.getRequestDispatcher("/views/admin-dashboard.jsp").forward(request, response);
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    private static User getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("user");
    }
}
