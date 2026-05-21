package com.ecofeast.dao;

import com.ecofeast.model.FoodCategory;
import com.ecofeast.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodCategoryDao {
    
    public List<FoodCategory> getAllCategories() throws SQLException {
        List<FoodCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM food_categories WHERE is_active = TRUE ORDER BY category_name";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                FoodCategory c = new FoodCategory();
                c.setCategoryId(rs.getInt("category_id"));
                c.setCategoryName(rs.getString("category_name"));
                c.setDescription(rs.getString("description"));
                c.setIconClass(rs.getString("icon_class"));
                c.setActive(rs.getBoolean("is_active"));
                if (rs.getTimestamp("created_at") != null) {
                    c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                list.add(c);
            }
        }
        return list;
    }
}
