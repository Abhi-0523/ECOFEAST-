package com.ecofeast.dao;

import com.ecofeast.model.FoodDonation;
import com.ecofeast.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDonationDao {

    public List<FoodDonation> getAllDonations() throws SQLException {
        List<FoodDonation> list = new ArrayList<>();
        String sql = "SELECT d.*, u.full_name AS donorName, c.category_name AS categoryName " +
                     "FROM food_donations d " +
                     "JOIN users u ON d.donor_id = u.user_id " +
                     "JOIN food_categories c ON d.category_id = c.category_id " +
                     "ORDER BY d.created_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<FoodDonation> getDonationsByDonor(int donorId) throws SQLException {
        List<FoodDonation> list = new ArrayList<>();
        String sql = "SELECT d.*, u.full_name AS donorName, c.category_name AS categoryName " +
                     "FROM food_donations d " +
                     "JOIN users u ON d.donor_id = u.user_id " +
                     "JOIN food_categories c ON d.category_id = c.category_id " +
                     "WHERE d.donor_id = ? " +
                     "ORDER BY d.created_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, donorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<FoodDonation> getAvailableDonations() throws SQLException {
        List<FoodDonation> list = new ArrayList<>();
        String sql = "SELECT d.*, u.full_name AS donorName, c.category_name AS categoryName " +
                     "FROM food_donations d " +
                     "JOIN users u ON d.donor_id = u.user_id " +
                     "JOIN food_categories c ON d.category_id = c.category_id " +
                     "WHERE d.status = 'AVAILABLE' AND d.expiry_time > NOW() " +
                     "ORDER BY d.expiry_time ASC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public FoodDonation getDonationById(int id) throws SQLException {
        String sql = "SELECT d.*, u.full_name AS donorName, c.category_name AS categoryName " +
                     "FROM food_donations d " +
                     "JOIN users u ON d.donor_id = u.user_id " +
                     "JOIN food_categories c ON d.category_id = c.category_id " +
                     "WHERE d.donation_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public int addDonation(FoodDonation d) throws SQLException {
        String sql = "INSERT INTO food_donations (donor_id, category_id, food_name, description, quantity, quantity_unit, expiry_time, pickup_location, pickup_city, image_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, d.getDonorId());
            stmt.setInt(2, d.getCategoryId());
            stmt.setString(3, d.getFoodName());
            stmt.setString(4, d.getDescription());
            stmt.setInt(5, d.getQuantity());
            stmt.setString(6, d.getQuantityUnit() != null ? d.getQuantityUnit() : "kg");
            stmt.setObject(7, d.getExpiryTime());
            stmt.setString(8, d.getPickupLocation());
            stmt.setString(9, d.getPickupCity());
            stmt.setString(10, d.getImageUrl());
            stmt.setString(11, d.getStatus() != null ? d.getStatus() : "AVAILABLE");
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    public boolean updateDonation(FoodDonation d) throws SQLException {
        String sql = "UPDATE food_donations SET category_id=?, food_name=?, description=?, quantity=?, quantity_unit=?, expiry_time=?, pickup_location=?, pickup_city=?, image_url=? " +
                     "WHERE donation_id=? AND donor_id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getCategoryId());
            stmt.setString(2, d.getFoodName());
            stmt.setString(3, d.getDescription());
            stmt.setInt(4, d.getQuantity());
            stmt.setString(5, d.getQuantityUnit() != null ? d.getQuantityUnit() : "kg");
            stmt.setObject(6, d.getExpiryTime());
            stmt.setString(7, d.getPickupLocation());
            stmt.setString(8, d.getPickupCity());
            stmt.setString(9, d.getImageUrl());
            stmt.setInt(10, d.getDonationId());
            stmt.setInt(11, d.getDonorId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteDonation(int donationId, int donorId) throws SQLException {
        String sql = "DELETE FROM food_donations WHERE donation_id=? AND donor_id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, donationId);
            stmt.setInt(2, donorId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<FoodDonation> searchDonations(String keyword, int categoryId, String city) throws SQLException {
        List<FoodDonation> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT d.*, u.full_name AS donorName, c.category_name AS categoryName " +
            "FROM food_donations d " +
            "JOIN users u ON d.donor_id = u.user_id " +
            "JOIN food_categories c ON d.category_id = c.category_id " +
            "WHERE d.status = 'AVAILABLE' AND d.expiry_time > NOW()"
        );
        
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (d.food_name LIKE ? OR d.description LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (categoryId > 0) {
            sql.append(" AND d.category_id = ?");
            params.add(categoryId);
        }
        if (city != null && !city.trim().isEmpty()) {
            sql.append(" AND d.pickup_city LIKE ?");
            params.add("%" + city + "%");
        }
        
        sql.append(" ORDER BY d.expiry_time ASC");
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public boolean updateStatus(int donationId, String status) throws SQLException {
        String sql = "UPDATE food_donations SET status = ? WHERE donation_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, donationId);
            return stmt.executeUpdate() > 0;
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM food_donations";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM food_donations WHERE status=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    private FoodDonation mapRow(ResultSet rs) throws SQLException {
        FoodDonation d = new FoodDonation();
        d.setDonationId(rs.getInt("donation_id"));
        d.setDonorId(rs.getInt("donor_id"));
        d.setCategoryId(rs.getInt("category_id"));
        d.setFoodName(rs.getString("food_name"));
        d.setDescription(rs.getString("description"));
        d.setQuantity(rs.getInt("quantity"));
        d.setQuantityUnit(rs.getString("quantity_unit"));
        if (rs.getTimestamp("expiry_time") != null) {
            d.setExpiryTime(rs.getTimestamp("expiry_time").toLocalDateTime());
        }
        d.setPickupLocation(rs.getString("pickup_location"));
        d.setPickupCity(rs.getString("pickup_city"));
        d.setImageUrl(rs.getString("image_url"));
        d.setStatus(rs.getString("status"));
        if (rs.getTimestamp("created_at") != null) {
            d.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        
        try { d.setDonorName(rs.getString("donorName")); } catch (Exception ignored) {}
        try { d.setCategoryName(rs.getString("categoryName")); } catch (Exception ignored) {}
        return d;
    }
}
