// StatisticsManager.java
package com.tasktracker.manager;

import com.tasktracker.dao.TaskDAO;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StatisticsManager {
    private TaskDAO taskDAO;

    public StatisticsManager() {
        this.taskDAO = new TaskDAO();
    }

    public Map<String, Integer> getCompletedTasksCountToday(int userId) {
        Map<String, Integer> stats = new HashMap<>();
        int count = taskDAO.getCompletedTasksCountForDate(userId, LocalDate.now());
        stats.put("today", count);
        return stats;
    }

    public Map<String, Integer> getCompletedTasksCountThisWeek(int userId) {
        Map<String, Integer> stats = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate endOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7);

        int count = taskDAO.getCompletedTasksCountForDateRange(userId, startOfWeek, endOfWeek);
        stats.put("thisWeek", count);
        return stats;
    }

    public Map<String, Integer> getTasksCountByCategory(int userId) {
        return taskDAO.getTaskCountByCategory(userId);
    }

    public Map<String, Integer> getTasksCountByPriority(int userId) {
        return taskDAO.getTaskCountByPriority(userId);
    }
}
