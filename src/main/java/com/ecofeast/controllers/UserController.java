package com.ecofeast.controllers;

import com.ecofeast.models.FoodItem;
import com.ecofeast.models.User;
import com.ecofeast.service.FoodService;
import com.ecofeast.service.UserService;
import com.ecofeast.util.DatabaseUtil;
import com.ecofeast.util.PasswordUtil;
import com.ecofeast.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserController - Handles user-specific operations.
 * Manages food item search, request submission, and user portal functionality.
 */
public class UserController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getSessionUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("portal".equals(action)) {
                request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
            } else if ("viewItem".equals(action)) {
                viewFoodItemDetails(request, response);
            } else if ("myRequests".equals(action)) {
                viewMyRequests(request, response, user);
            } else if ("availableItems".equals(action)) {
                viewAvailableItems(request, response);
            } else if ("viewWishlist".equals(action)) {
                request.getRequestDispatcher("/views/user-wishlist.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("User GET error: " + e.getMessage());
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getSessionUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("requestFood".equals(action)) {
                submitFoodRequest(request, response, user);
            } else if ("updateProfile".equals(action)) {
                updateUserProfile(request, response, user);
            } else if ("changePassword".equals(action)) {
                changeUserPassword(request, response, user);
            } else if ("cancelRequest".equals(action)) {
                cancelRequest(request, response);
            } else if ("addToWishlist".equals(action)) {
                addToWishlist(request, response);
            } else if ("removeFromWishlist".equals(action)) {
                removeFromWishlist(request, response);
            }
        } catch (Exception e) {
            System.err.println("User POST error: " + e.getMessage());
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
        }
    }

    private void viewAvailableItems(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String searchQuery = request.getParameter("search");
        request.setAttribute("foodItems", FoodService.searchAvailableFoodItems(searchQuery));
        request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
    }

    private void addToWishlist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        FoodItem item = FoodService.getFoodItemById(itemId);
        if (item != null) {
            HttpSession session = request.getSession();
            @SuppressWarnings("unchecked")
            List<FoodItem> wishlist = (List<FoodItem>) session.getAttribute("wishlist");
            if (wishlist == null) {
                wishlist = new ArrayList<>();
            }
            wishlist.add(item);
            session.setAttribute("wishlist", wishlist);
            request.setAttribute("success", "Item added to wishlist!");
        }
        viewAvailableItems(request, response);
    }

    private void removeFromWishlist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<FoodItem> wishlist = (List<FoodItem>) session.getAttribute("wishlist");
        if (wishlist != null) {
            wishlist.removeIf(item -> item.getItemId() == itemId);
            session.setAttribute("wishlist", wishlist);
        }
        request.getRequestDispatcher("/views/user-wishlist.jsp").forward(request, response);
    }

    private void viewFoodItemDetails(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String itemIdStr = request.getParameter("itemId");
        if (!ValidationUtil.isNotEmpty(itemIdStr)) {
            request.setAttribute("error", "Item ID is required");
            request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
            return;
        }
        int itemId = Integer.parseInt(itemIdStr);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM food_items WHERE item_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, itemId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                request.setAttribute("foodItem", mapFoodItemRow(rs));
                request.getRequestDispatcher("/views/food-item-details.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Food item not found");
                request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    private void submitFoodRequest(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        String itemIdStr = request.getParameter("itemId");
        String quantityStr = request.getParameter("quantity");
        if (!ValidationUtil.isNotEmpty(itemIdStr) || !ValidationUtil.isNotEmpty(quantityStr)) {
            request.setAttribute("error", "Item ID and quantity are required");
            request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
            return;
        }
        int itemId = Integer.parseInt(itemIdStr);
        int quantity = Integer.parseInt(quantityStr);
        if (!ValidationUtil.isValidQuantity(quantity)) {
            request.setAttribute("error", "Quantity must be greater than 0");
            request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
            return;
        }
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            String insertSql = "INSERT INTO requests (user_id, item_id, quantity_requested, status) VALUES (?, ?, ?, 'PENDING')";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, user.getUserId());
                pstmt.setInt(2, itemId);
                pstmt.setInt(3, quantity);
                if (pstmt.executeUpdate() > 0) {
                    request.setAttribute("success", "Request submitted successfully!");
                } else {
                    request.setAttribute("error", "Failed to submit request");
                }
            }
        } finally {
            if (conn != null) conn.close();
        }
        request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
    }

    private void viewMyRequests(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT r.*, f.item_name, f.category FROM requests r " +
                    "JOIN food_items f ON r.item_id = f.item_id " +
                    "WHERE r.user_id = ? ORDER BY r.request_date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user.getUserId());
            rs = pstmt.executeQuery();
            request.setAttribute("userRequests", rs);
            request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNumber = request.getParameter("phoneNumber");
        if (!ValidationUtil.isNotEmpty(firstName) || !ValidationUtil.isNotEmpty(lastName)) {
            request.setAttribute("error", "First name and last name are required");
            request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
            return;
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        if (UserService.updateProfile(user)) {
            request.setAttribute("success", "Profile updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update profile");
        }
        request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
    }

    private void changeUserPassword(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        if (!PasswordUtil.verifyPassword(oldPassword, user.getPassword())) {
            request.setAttribute("error", "Current password is incorrect");
            request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
            return;
        }
        String hashed = PasswordUtil.hashPassword(newPassword);
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE users SET password = ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hashed);
            pstmt.setInt(2, user.getUserId());
            if (pstmt.executeUpdate() > 0) {
                user.setPassword(hashed);
                request.setAttribute("success", "Password changed successfully!");
            } else {
                request.setAttribute("error", "Failed to update password");
            }
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
        request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
    }

    private void cancelRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String requestIdStr = request.getParameter("requestId");
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM requests WHERE request_id = ? AND status = 'PENDING'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(requestIdStr));
            if (pstmt.executeUpdate() > 0) {
                request.setAttribute("success", "Request cancelled successfully!");
            } else {
                request.setAttribute("error", "Failed to cancel request");
            }
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
        request.getRequestDispatcher("/views/user-portal.jsp").forward(request, response);
    }

    private static FoodItem mapFoodItemRow(ResultSet rs) throws SQLException {
        FoodItem item = new FoodItem();
        item.setItemId(rs.getInt("item_id"));
        item.setItemName(rs.getString("item_name"));
        item.setDescription(rs.getString("description"));
        item.setCategory(rs.getString("category"));
        item.setQuantity(rs.getInt("quantity"));
        if (rs.getDate("expiry_date") != null) {
            item.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
        }
        item.setLocation(rs.getString("location"));
        item.setStatus(rs.getString("status"));
        return item;
    }

    private static User getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("user") : null;
    }
}
