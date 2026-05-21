package com.ecofeast.dao;

import com.ecofeast.model.VolunteerTask;
import com.ecofeast.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerTaskDao {

    public int createTask(VolunteerTask task) throws SQLException {
        String sql = "INSERT INTO volunteer_tasks (request_id, task_type, pickup_address, delivery_address, status) " +
                     "VALUES (?, ?, ?, ?, 'OPEN')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, task.getRequestId());
            stmt.setString(2, task.getTaskType());
            stmt.setString(3, task.getPickupAddress());
            stmt.setString(4, task.getDeliveryAddress());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM volunteer_tasks WHERE status=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<VolunteerTask> getOpenTasks() throws SQLException {
        List<VolunteerTask> list = new ArrayList<>();
        String sql = "SELECT t.*, d.food_name, u.full_name AS ngoName, du.full_name AS donorName " +
                     "FROM volunteer_tasks t " +
                     "JOIN donation_requests r ON t.request_id = r.request_id " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "JOIN users u ON r.ngo_id = u.user_id " +
                     "JOIN users du ON d.donor_id = du.user_id " +
                     "WHERE t.status = 'OPEN' " +
                     "ORDER BY t.created_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<VolunteerTask> getTasksByVolunteer(int volunteerId) throws SQLException {
        List<VolunteerTask> list = new ArrayList<>();
        String sql = "SELECT t.*, d.food_name, u.full_name AS ngoName, du.full_name AS donorName " +
                     "FROM volunteer_tasks t " +
                     "JOIN donation_requests r ON t.request_id = r.request_id " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "JOIN users u ON r.ngo_id = u.user_id " +
                     "JOIN users du ON d.donor_id = du.user_id " +
                     "WHERE t.volunteer_id = ? " +
                     "ORDER BY t.updated_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, volunteerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<VolunteerTask> getAllTasks() throws SQLException {
        List<VolunteerTask> list = new ArrayList<>();
        String sql = "SELECT t.*, d.food_name, u.full_name AS ngoName, du.full_name AS donorName, vu.full_name AS volunteerName " +
                     "FROM volunteer_tasks t " +
                     "JOIN donation_requests r ON t.request_id = r.request_id " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "JOIN users u ON r.ngo_id = u.user_id " +
                     "JOIN users du ON d.donor_id = du.user_id " +
                     "LEFT JOIN users vu ON t.volunteer_id = vu.user_id " +
                     "ORDER BY t.created_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                VolunteerTask task = mapRow(rs);
                try { task.setVolunteerName(rs.getString("volunteerName")); } catch (Exception ignored) {}
                list.add(task);
            }
        }
        return list;
    }
    
    public VolunteerTask getTaskById(int taskId) throws SQLException {
        String sql = "SELECT t.*, d.food_name, u.full_name AS ngoName, du.full_name AS donorName " +
                     "FROM volunteer_tasks t " +
                     "JOIN donation_requests r ON t.request_id = r.request_id " +
                     "JOIN food_donations d ON r.donation_id = d.donation_id " +
                     "JOIN users u ON r.ngo_id = u.user_id " +
                     "JOIN users du ON d.donor_id = du.user_id " +
                     "WHERE t.task_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public boolean acceptTask(int taskId, int volunteerId) throws SQLException {
        String sql = "UPDATE volunteer_tasks SET status='ACCEPTED', volunteer_id=?, accepted_at=NOW() WHERE task_id=? AND status='OPEN'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, volunteerId);
            stmt.setInt(2, taskId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean startTask(int taskId, int volunteerId) throws SQLException {
        String sql = "UPDATE volunteer_tasks SET status='IN_PROGRESS' WHERE task_id=? AND volunteer_id=? AND status='ACCEPTED'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.setInt(2, volunteerId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean completeTask(int taskId, int volunteerId) throws SQLException {
        String sql = "UPDATE volunteer_tasks SET status='COMPLETED', completed_at=NOW() WHERE task_id=? AND volunteer_id=? AND status='IN_PROGRESS'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.setInt(2, volunteerId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateTaskStatus(int taskId, int volunteerId, String status) throws SQLException {
        String sql;
        if ("ACCEPTED".equals(status)) {
            return acceptTask(taskId, volunteerId);
        } else if ("COMPLETED".equals(status)) {
            return completeTask(taskId, volunteerId);
        } else {
            sql = "UPDATE volunteer_tasks SET status=? WHERE task_id=?";
        }
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, taskId);
            return stmt.executeUpdate() > 0;
        }
    }

    private VolunteerTask mapRow(ResultSet rs) throws SQLException {
        VolunteerTask t = new VolunteerTask();
        t.setTaskId(rs.getInt("task_id"));
        t.setRequestId(rs.getInt("request_id"));
        int volId = rs.getInt("volunteer_id");
        if (!rs.wasNull()) t.setVolunteerId(volId);
        t.setTaskType(rs.getString("task_type"));
        t.setPickupAddress(rs.getString("pickup_address"));
        t.setDeliveryAddress(rs.getString("delivery_address"));
        t.setStatus(rs.getString("status"));
        
        if (rs.getTimestamp("accepted_at") != null) t.setAcceptedAt(rs.getTimestamp("accepted_at").toLocalDateTime());
        if (rs.getTimestamp("completed_at") != null) t.setCompletedAt(rs.getTimestamp("completed_at").toLocalDateTime());
        if (rs.getTimestamp("created_at") != null) t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        try { t.setFoodName(rs.getString("food_name")); } catch (Exception ignored) {}
        try { t.setNgoName(rs.getString("ngoName")); } catch (Exception ignored) {}
        try { t.setDonorName(rs.getString("donorName")); } catch (Exception ignored) {}
        return t;
    }
}
