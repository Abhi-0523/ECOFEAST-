package com.ecofeast.dao;

import com.ecofeast.model.ContactMessage;
import com.ecofeast.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactMessageDao {

    public boolean saveMessage(ContactMessage msg) throws SQLException {
        return createMessage(msg);
    }

    public boolean createMessage(ContactMessage msg) throws SQLException {
        String sql = "INSERT INTO contact_messages (sender_name, sender_email, subject, message) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, msg.getSenderName());
            stmt.setString(2, msg.getSenderEmail());
            stmt.setString(3, msg.getSubject());
            stmt.setString(4, msg.getMessage());
            return stmt.executeUpdate() > 0;
        }
    }
}
