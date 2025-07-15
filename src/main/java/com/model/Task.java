// Task.java
package com.tasktracker.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Task {
    private int id;
    private int userId;
    private String title;
    private String description;
    private Date dueDate;
    private int priorityId;
    private int categoryId;
    private boolean isCompleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Campi aggiuntivi per facilitare la visualizzazione nelle JSP
    private String categoryName;
    private String priorityName;

    public Task() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    @Override
    public String toString() {
        return "Task{" +
               "id=" + id +
               ", userId=" + userId +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", dueDate=" + dueDate +
               ", priorityId=" + priorityId +
               ", categoryId=" + categoryId +
               ", isCompleted=" + isCompleted +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", categoryName='" + categoryName + '\'' +
               ", priorityName='" + priorityName + '\'' +
               '}';
    }
}
