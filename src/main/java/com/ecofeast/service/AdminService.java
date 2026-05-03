package com.ecofeast.service;

import com.ecofeast.models.User;
import com.ecofeast.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminService {

    public static List<User> getPendingUsers() throws Exception {
        List<User> pendingUsers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM users WHERE account_status = 'PENDING'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                pendingUsers.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        "",
                        rs.getString("role"),
                        rs.getString("account_status"),
                        rs.getBoolean("is_active")
                ));
            }
            return pendingUsers;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public static boolean approveUser(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE users SET account_status = 'APPROVED', is_active = TRUE WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public static Map<String, Object> getReports() throws Exception {
        Map<String, Object> reports = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            conn = DatabaseUtil.getConnection();
            
            // Query 1: Most requested food items
            String sql1 = "SELECT f.item_name, COUNT(r.request_id) as req_count " +
                          "FROM food_items f LEFT JOIN requests r ON f.item_id = r.item_id " +
                          "GROUP BY f.item_id ORDER BY req_count DESC LIMIT 5";
            pstmt1 = conn.prepareStatement(sql1);
            rs1 = pstmt1.executeQuery();
            
            Map<String, Integer> popularItems = new HashMap<>();
            while (rs1.next()) {
                popularItems.put(rs1.getString("item_name"), rs1.getInt("req_count"));
            }
            reports.put("popularItems", popularItems);

            // Query 2: Availability vs Requests
            String sql2 = "SELECT status, COUNT(*) as count FROM food_items GROUP BY status";
            pstmt2 = conn.prepareStatement(sql2);
            rs2 = pstmt2.executeQuery();
            
            Map<String, Integer> availabilityStatus = new HashMap<>();
            while (rs2.next()) {
                availabilityStatus.put(rs2.getString("status"), rs2.getInt("count"));
            }
            reports.put("availabilityStatus", availabilityStatus);

            return reports;
        } finally {
            if (rs1 != null) rs1.close();
            if (rs2 != null) rs2.close();
            if (pstmt1 != null) pstmt1.close();
            if (pstmt2 != null) pstmt2.close();
            if (conn != null) conn.close();
        }
    }
}
