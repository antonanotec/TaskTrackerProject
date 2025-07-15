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
        this.taskDAO = new TaskDAO(); // Assicurati che TaskDAO abbia un costruttore senza argomenti o passi una connessione
    }

    [cite_start]// Metodo per il conteggio delle attività completate oggi [cite: 157]
    public Map<String, Integer> getCompletedTasksCountToday(int userId) {
        Map<String, Integer> stats = new HashMap<>();
        // Assumendo che TaskDAO abbia un metodo getCompletedTasksCountForDate(userId, date)
        int count = taskDAO.getCompletedTasksCountForDate(userId, LocalDate.now());
        stats.put("today", count);
        return stats;
    }

    [cite_start]// Metodo per il conteggio delle attività completate questa settimana [cite: 157]
    public Map<String, Integer> getCompletedTasksCountThisWeek(int userId) {
        Map<String, Integer> stats = new HashMap<>();
        // Calcola l'inizio e la fine della settimana corrente
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1); // Lunedì
        LocalDate endOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7); // Domenica

        // Assumendo che TaskDAO abbia un metodo getCompletedTasksCountForDateRange(userId, startDate, endDate)
        int count = taskDAO.getCompletedTasksCountForDateRange(userId, startOfWeek, endOfWeek);
        stats.put("thisWeek", count);
        return stats;
    }

    [cite_start]// Metodo per la distribuzione delle attività per categoria [cite: 158]
    public Map<String, Integer> getTasksCountByCategory(int userId) {
        return taskDAO.getTaskCountByCategory(userId);
    }

    [cite_start]// Metodo per la distribuzione delle attività per priorità [cite: 159]
    public Map<String, Integer> getTasksCountByPriority(int userId) {
        return taskDAO.getTaskCountByPriority(userId);
    }
}