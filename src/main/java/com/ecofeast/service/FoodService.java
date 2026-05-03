package com.ecofeast.service;

import com.ecofeast.models.FoodItem;
import com.ecofeast.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FoodService {

    public static FoodItem getFoodItemById(int itemId) throws Exception {
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
                FoodItem item = new FoodItem();
                item.setItemId(rs.getInt("item_id"));
                item.setAdminId(rs.getInt("admin_id"));
                item.setItemName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setCategory(rs.getString("category"));
                item.setQuantity(rs.getInt("quantity"));
                if (rs.getDate("expiry_date") != null) {
                    item.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                }
                item.setLocation(rs.getString("location"));
                item.setImageUrl(rs.getString("image_url"));
                item.setStatus(rs.getString("status"));
                return item;
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public static List<FoodItem> searchAvailableFoodItems(String query) throws Exception {
        List<FoodItem> items = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM food_items WHERE status = 'AVAILABLE'";
            if (query != null && !query.trim().isEmpty()) {
                sql += " AND (item_name LIKE ? OR category LIKE ?)";
            }
            sql += " ORDER BY expiry_date ASC";
            
            pstmt = conn.prepareStatement(sql);
            
            if (query != null && !query.trim().isEmpty()) {
                pstmt.setString(1, "%" + query + "%");
                pstmt.setString(2, "%" + query + "%");
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                FoodItem item = new FoodItem();
                item.setItemId(rs.getInt("item_id"));
                item.setAdminId(rs.getInt("admin_id"));
                item.setItemName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setCategory(rs.getString("category"));
                item.setQuantity(rs.getInt("quantity"));
                if (rs.getDate("expiry_date") != null) {
                    item.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                }
                item.setLocation(rs.getString("location"));
                item.setImageUrl(rs.getString("image_url"));
                item.setStatus(rs.getString("status"));
                items.add(item);
            }
            return items;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
}
