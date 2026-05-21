package com.ecofeast.dao;

import com.ecofeast.model.Notification;
import com.ecofeast.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {

    public boolean createNotification(Notification notif) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, title, message, type, link_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notif.getUserId());
            stmt.setString(2, notif.getTitle());
            stmt.setString(3, notif.getMessage());
            stmt.setString(4, notif.getType());
            stmt.setString(5, notif.getLinkUrl());
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Notification> getNotificationsByUser(int userId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC LIMIT 20";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    n.setNotificationId(rs.getInt("notification_id"));
                    n.setUserId(rs.getInt("user_id"));
                    n.setTitle(rs.getString("title"));
                    n.setMessage(rs.getString("message"));
                    n.setType(rs.getString("type"));
                    n.setRead(rs.getBoolean("is_read"));
                    n.setLinkUrl(rs.getString("link_url"));
                    if (rs.getTimestamp("created_at") != null) n.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(n);
                }
            }
        }
        return list;
    }

    public boolean markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;
        }
    }
}
