package com.ecofeast.dao;

import com.ecofeast.model.Role;
import com.ecofeast.model.User;
import com.ecofeast.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDao - Data Access Object for 'users' and 'roles' tables.
 */
public class UserDao {

    /**
     * Role name via subquery so login still works if an INNER JOIN to {@code roles} would drop rows
     * (e.g. orphaned {@code role_id} in older databases). Email match is case-insensitive.
     */
    private static final String ROLE_SUBQUERY =
            "(SELECT r.role_name FROM roles r WHERE r.role_id = u.role_id LIMIT 1) AS role_name";

    /** Creates a new user during registration. */
    public int createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (role_id, full_name, email, phone, password_hash, organization, address, city, state, zip_code, account_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, user.getRoleId());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getPasswordHash());
            ps.setString(6, user.getOrganization());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getCity());
            ps.setString(9, user.getState());
            ps.setString(10, user.getZipCode());
            ps.setString(11, user.getAccountStatus() != null ? user.getAccountStatus() : "PENDING");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    /** Finds a user by email (for login). */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT u.*, " + ROLE_SUBQUERY + " FROM users u WHERE LOWER(TRIM(u.email)) = LOWER(TRIM(?))";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        }
        return null;
    }

    /** Finds a user by ID. */
    public User findById(int id) throws SQLException {
        String sql = "SELECT u.*, " + ROLE_SUBQUERY + " FROM users u WHERE u.user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        }
        return null;
    }

    /** Updates editable profile fields for the given user. */
    public boolean updateProfile(User user) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, phone = ?, organization = ?, address = ?, "
                + "city = ?, state = ?, zip_code = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getOrganization());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getCity());
            ps.setString(6, user.getState());
            ps.setString(7, user.getZipCode());
            ps.setInt(8, user.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /** Updates user account status (Admin approval). */
    public boolean updateStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE users SET account_status = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Gets users by status (e.g., PENDING). */
    public List<User> getUsersByStatus(String status) throws SQLException {
        String sql = "SELECT u.*, " + ROLE_SUBQUERY + " FROM users u WHERE u.account_status = ?";
        List<User> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapUser(rs));
        }
        return list;
    }
    
    /** Gets role ID by role name. */
    public int getRoleIdByName(String roleName) throws SQLException {
        String sql = "SELECT role_id FROM roles WHERE UPPER(TRIM(role_name)) = UPPER(TRIM(?))";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("role_id");
        }
        return -1;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("user_id"));
        u.setRoleId(rs.getInt("role_id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setOrganization(rs.getString("organization"));
        u.setAddress(rs.getString("address"));
        u.setCity(rs.getString("city"));
        u.setState(rs.getString("state"));
        u.setZipCode(rs.getString("zip_code"));
        u.setAccountStatus(rs.getString("account_status"));
        u.setActive(rs.getBoolean("is_active"));

        Role r = new Role(u.getRoleId(), rs.getString("role_name"));
        u.setRole(r);

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toLocalDateTime());

        return u;
    }
}
