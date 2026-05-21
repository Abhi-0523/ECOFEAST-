package com.ecofeast.dao;

import com.ecofeast.model.DonationRequest;
import com.ecofeast.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationRequestDao {

    public int addRequest(DonationRequest req) throws SQLException {
        String sql = "INSERT INTO donation_requests (donation_id, ngo_id, quantity_requested, request_message, status) " +
                     "VALUES (?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, req.getDonationId());
            stmt.setInt(2, req.getNgoId());
            stmt.setInt(3, req.getQuantityRequested());
            stmt.setString(4, req.getRequestMessage());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        req.setRequestId(id);
                        return id;
                    }
                }
            }
            return -1;
        }
    }

    public boolean hasAlreadyRequested(int ngoId, int donationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM donation_requests WHERE ngo_id=? AND donation_id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ngoId);
            stmt.setInt(2, donationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean approveRequest(int requestId, int donorId) throws SQLException {
        String sql = "UPDATE donation_requests r " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "SET r.status='APPROVED', r.responded_at=NOW() " +
                     "WHERE r.request_id=? AND d.donor_id=? AND r.status='PENDING'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.setInt(2, donorId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean rejectRequest(int requestId, int donorId, String reason) throws SQLException {
        String sql = "UPDATE donation_requests r " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "SET r.status='REJECTED', r.rejection_reason=?, r.responded_at=NOW() " +
                     "WHERE r.request_id=? AND d.donor_id=? AND r.status='PENDING'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reason);
            stmt.setInt(2, requestId);
            stmt.setInt(3, donorId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean cancelRequest(int requestId, int ngoId) throws SQLException {
        String sql = "UPDATE donation_requests SET status='CANCELLED' WHERE request_id=? AND ngo_id=? AND status='PENDING'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.setInt(2, ngoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean markCollected(int requestId) throws SQLException {
        String sql = "UPDATE donation_requests SET status='COLLECTED', collected_at=NOW() " +
                     "WHERE request_id=? AND status='APPROVED'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM donation_requests WHERE status=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<DonationRequest> getAllRequests() throws SQLException {
        List<DonationRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, d.food_name, u.full_name AS ngoName, du.full_name AS donorName " +
                     "FROM donation_requests r " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "JOIN users u ON r.ngo_id = u.user_id " +
                     "JOIN users du ON d.donor_id = du.user_id " +
                     "ORDER BY r.requested_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<DonationRequest> getRequestsByNgo(int ngoId) throws SQLException {
        List<DonationRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, d.food_name, u.full_name AS ngoName, du.full_name AS donorName " +
                     "FROM donation_requests r " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "JOIN users u ON r.ngo_id = u.user_id " +
                     "JOIN users du ON d.donor_id = du.user_id " +
                     "WHERE r.ngo_id = ? " +
                     "ORDER BY r.requested_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ngoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<DonationRequest> getRequestsByDonor(int donorId) throws SQLException {
        List<DonationRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, d.food_name, u.full_name AS ngoName, du.full_name AS donorName " +
                     "FROM donation_requests r " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "JOIN users u ON r.ngo_id = u.user_id " +
                     "JOIN users du ON d.donor_id = du.user_id " +
                     "WHERE d.donor_id = ? " +
                     "ORDER BY r.requested_at DESC";
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
    
    public DonationRequest getRequestById(int requestId) throws SQLException {
        String sql = "SELECT r.*, d.food_name, u.full_name AS ngoName, du.full_name AS donorName " +
                     "FROM donation_requests r " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "JOIN users u ON r.ngo_id = u.user_id " +
                     "JOIN users du ON d.donor_id = du.user_id " +
                     "WHERE r.request_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    private DonationRequest mapRow(ResultSet rs) throws SQLException {
        DonationRequest r = new DonationRequest();
        r.setRequestId(rs.getInt("request_id"));
        r.setDonationId(rs.getInt("donation_id"));
        r.setNgoId(rs.getInt("ngo_id"));
        r.setQuantityRequested(rs.getInt("quantity_requested"));
        r.setRequestMessage(rs.getString("request_message"));
        r.setStatus(rs.getString("status"));
        r.setRejectionReason(rs.getString("rejection_reason"));
        
        if (rs.getTimestamp("requested_at") != null) r.setRequestedAt(rs.getTimestamp("requested_at").toLocalDateTime());
        if (rs.getTimestamp("responded_at") != null) r.setRespondedAt(rs.getTimestamp("responded_at").toLocalDateTime());
        if (rs.getTimestamp("collected_at") != null) r.setCollectedAt(rs.getTimestamp("collected_at").toLocalDateTime());
        
        try { r.setNgoName(rs.getString("ngoName")); } catch (Exception ignored) {}
        try { r.setFoodName(rs.getString("food_name")); } catch (Exception ignored) {}
        try { r.setDonorName(rs.getString("donorName")); } catch (Exception ignored) {}
        return r;
    }
}
