// priority_dao.java
package com.tasktracker.dao;

import com.tasktracker.model.Priority;
import com.tasktracker.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PriorityDAO {

    public List<Priority> getAllPriorities() {
        List<Priority> priorities = new ArrayList<>();
        String sql = "SELECT id, name FROM priorities ORDER BY name ASC;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                priorities.add(new Priority(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priorities;
    }

    public Priority getPriorityById(int id) {
        String sql = "SELECT id, name FROM priorities WHERE id = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Priority(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}