// stats_servlet.java
package com.tasktracker.servlet;

import com.tasktracker.manager.StatisticsManager;
import com.tasktracker.model.User;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/app/stats")
public class StatsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StatisticsManager statisticsManager;

    public void init() {
        statisticsManager = new StatisticsManager();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try {
            [cite_start]// Calcola le statistiche [cite: 156]
            Map<String, Integer> completedTasksToday = statisticsManager.getCompletedTasksCountToday(userId);
            Map<String, Integer> completedTasksThisWeek = statisticsManager.getCompletedTasksCountThisWeek(userId);
            Map<String, Integer> tasksByCategory = statisticsManager.getTasksCountByCategory(userId);
            Map<String, Integer> tasksByPriority = statisticsManager.getTasksCountByPriority(userId);

            request.setAttribute("completedTasksToday", completedTasksToday); [cite_start]// Attività completate oggi/settimana [cite: 157]
            request.setAttribute("completedTasksThisWeek", completedTasksThisWeek);
            request.setAttribute("tasksByCategory", tasksByCategory); [cite_start]// Distribuzione per categoria [cite: 158]
            request.setAttribute("tasksByPriority", tasksByPriority); [cite_start]// Distribuzione per priorità [cite: 159]

            request.getRequestDispatcher("/WEB-INF/views/stats.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante il recupero delle statistiche: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        }
    }
}