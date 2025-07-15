// task_dao.java (Solo le aggiunte o modifiche necessarie per i metodi di StatisticsManager)
package com.tasktracker.dao;

import com.tasktracker.model.Category;
import com.tasktracker.model.Priority;
import com.tasktracker.model.Task;
import com.tasktracker.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDAO {
    // ... (metodi esistenti: addTask, getTaskById, updateTask, deleteTask, getAllUserTasks)

    [cite_start]// Nuovo metodo per filtrare per priorità [cite: 148]
    public List<Task> getTasksByPriority(int userId, int priorityId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, c.name AS category_name, p.name AS priority_name " +
                     "FROM tasks t JOIN categories c ON t.category_id = c.id " +
                     "JOIN priorities p ON t.priority_id = p.id " +
                     "WHERE t.user_id = ? AND t.priority_id = ? ORDER BY t.due_date ASC, t.priority_id DESC;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, priorityId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    [cite_start]// Nuovo metodo per filtrare per categoria [cite: 37]
    public List<Task> getTasksByCategory(int userId, int categoryId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, c.name AS category_name, p.name AS priority_name " +
                     "FROM tasks t JOIN categories c ON t.category_id = c.id " +
                     "JOIN priorities p ON t.priority_id = p.id " +
                     "WHERE t.user_id = ? AND t.category_id = ? ORDER BY t.due_date ASC, t.priority_id DESC;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, categoryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    [cite_start]// Nuovo metodo per filtrare per stato di completamento [cite: 39]
    public List<Task> getTasksByCompletionStatus(int userId, boolean isCompleted) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, c.name AS category_name, p.name AS priority_name " +
                     "FROM tasks t JOIN categories c ON t.category_id = c.id " +
                     "JOIN priorities p ON t.priority_id = p.id " +
                     "WHERE t.user_id = ? AND t.is_completed = ? ORDER BY t.due_date ASC, t.priority_id DESC;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setBoolean(2, isCompleted);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Nuovo metodo per aggiornare lo stato di completamento
    public void updateTaskCompletionStatus(int taskId, boolean isCompleted) throws SQLException {
        String sql = "UPDATE tasks SET is_completed = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isCompleted);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
        }
    }

    // Metodo per conteggiare le attività completate in una data specifica (per StatisticsManager)
    public int getCompletedTasksCountForDate(int userId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND is_completed = TRUE AND DATE(updated_at) = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Metodo per conteggiare le attività completate in un intervallo di date (per StatisticsManager)
    public int getCompletedTasksCountForDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND is_completed = TRUE AND DATE(updated_at) BETWEEN ? AND ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(startDate));
            pstmt.setDate(3, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Metodo per ottenere il conteggio delle attività per categoria (per StatisticsManager)
    public Map<String, Integer> getTaskCountByCategory(int userId) {
        Map<String, Integer> categoryCounts = new HashMap<>();
        String sql = "SELECT c.name, COUNT(t.id) FROM tasks t JOIN categories c ON t.category_id = c.id WHERE t.user_id = ? GROUP BY c.name;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                categoryCounts.put(rs.getString("name"), rs.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryCounts;
    }

    // Metodo per ottenere il conteggio delle attività per priorità (per StatisticsManager)
    public Map<String, Integer> getTaskCountByPriority(int userId) {
        Map<String, Integer> priorityCounts = new HashMap<>();
        String sql = "SELECT p.name, COUNT(t.id) FROM tasks t JOIN priorities p ON t.priority_id = p.id WHERE t.user_id = ? GROUP BY p.name;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                priorityCounts.put(rs.getString("name"), rs.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priorityCounts;
    }

    // Helper method to map ResultSet to Task object (assuming it exists in your TaskDAO)
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setUserId(rs.getInt("user_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setDueDate(rs.getDate("due_date"));
        task.setPriorityId(rs.getInt("priority_id"));
        task.setCategoryId(rs.getInt("category_id"));
        task.setCompleted(rs.getBoolean("is_completed"));
        task.setCreatedAt(rs.getTimestamp("created_at"));
        task.setUpdatedAt(rs.getTimestamp("updated_at"));
        // Aggiungi anche i nomi della categoria e della priorità per la visualizzazione
        task.setCategoryName(rs.getString("category_name"));
        task.setPriorityName(rs.getString("priority_name"));
        return task;
    }
}