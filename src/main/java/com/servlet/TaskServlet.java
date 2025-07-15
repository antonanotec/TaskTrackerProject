// task_servlet.java
package com.tasktracker.servlet;

import com.tasktracker.dao.TaskDAO;
import com.tasktracker.dao.CategoryDAO;
import com.tasktracker.dao.PriorityDAO;
import com.tasktracker.model.Task;
import com.tasktracker.model.Category;
import com.tasktracker.model.Priority;
import com.tasktracker.util.ValidationUtils;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/app/tasks", "/app/tasks/create", "/app/tasks/edit", "/app/tasks/delete", "/app/tasks/complete"})
public class TaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TaskDAO taskDAO;
    private CategoryDAO categoryDAO;
    private PriorityDAO priorityDAO;

    public void init() {
        taskDAO = new TaskDAO();
        categoryDAO = new CategoryDAO();
        priorityDAO = new PriorityDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String path = request.getServletPath();

        try {
            List<Category> categories = categoryDAO.getAllCategories();
            List<Priority> priorities = priorityDAO.getAllPriorities();
            request.setAttribute("categories", categories);
            request.setAttribute("priorities", priorities);

            if ("/app/tasks".equals(path)) {
                String filterPriority = request.getParameter("priority");
                String filterCategory = request.getParameter("category");
                String filterCompleted = request.getParameter("completed");

                List<Task> tasks;
                if (filterPriority != null && !filterPriority.isEmpty()) {
                    tasks = taskDAO.getTasksByPriority(userId, Integer.parseInt(filterPriority)); [cite_start]// Filtra per priorità [cite: 148]
                } else if (filterCategory != null && !filterCategory.isEmpty()) {
                    tasks = taskDAO.getTasksByCategory(userId, Integer.parseInt(filterCategory)); [cite_start]// Filtra per categoria [cite: 37]
                } else if (filterCompleted != null && !filterCompleted.isEmpty()) {
                    boolean isCompleted = Boolean.parseBoolean(filterCompleted);
                    tasks = taskDAO.getTasksByCompletionStatus(userId, isCompleted); [cite_start]// Visualizza completate/in sospeso [cite: 39]
                } else {
                    tasks = taskDAO.getAllUserTasks(userId);
                }
                request.setAttribute("tasks", tasks);
                request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
            } else if ("/app/tasks/create".equals(path)) {
                request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
            } else if ("/app/tasks/edit".equals(path)) {
                int taskId = Integer.parseInt(request.getParameter("id"));
                Task task = taskDAO.getTaskById(taskId, userId);
                if (task != null) {
                    request.setAttribute("task", task);
                    request.getRequestDispatcher("/WEB-INF/views/edit_task.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task non trovata o non autorizzato.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante il recupero delle attività: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        int userId = (int) session.getAttribute("userId");
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/app/tasks/create":
                    createTask(request, response, userId); [cite_start]// Creazione attività [cite: 136]
                    break;
                case "/app/tasks/edit":
                    updateTask(request, response, userId); [cite_start]// Modifica attività [cite: 30]
                    break;
                case "/app/tasks/delete":
                    deleteTask(request, response, userId); [cite_start]// Eliminazione attività [cite: 31]
                    break;
                case "/app/tasks/complete":
                    toggleTaskCompletion(request, response, userId); [cite_start]// Segna come completata/non completata [cite: 32]
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante l'operazione: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/app/tasks?errorMessage=" + e.getMessage());
        }
    }

    private void createTask(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDateStr = request.getParameter("due_date");
        int priorityId = Integer.parseInt(request.getParameter("priority"));
        int categoryId = Integer.parseInt(request.getParameter("category"));

        [cite_start]// Validazione dei dati [cite: 135]
        if (!ValidationUtils.isValidTaskTitle(title) || !ValidationUtils.isValidDueDate(dueDateStr)) {
            request.setAttribute("errorMessage", "Dati attività non validi. Titolo obbligatorio, data nel formato YYYY-MM-DD.");
            List<Category> categories = categoryDAO.getAllCategories();
            List<Priority> priorities = priorityDAO.getAllPriorities();
            request.setAttribute("categories", categories);
            request.setAttribute("priorities", priorities);
            request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
            return;
        }

        try {
            Task newTask = new Task();
            newTask.setUserId(userId);
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setDueDate(Date.valueOf(dueDateStr));
            newTask.setPriorityId(priorityId);
            newTask.setCategoryId(categoryId);
            newTask.setCompleted(false);

            taskDAO.addTask(newTask);
            response.sendRedirect(request.getContextPath() + "/app/tasks?message=taskCreated"); [cite_start]// Conferma creazione [cite: 137]
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante la creazione dell'attività: " + e.getMessage()); [cite_start]// Errore di salvataggio [cite: 141]
            List<Category> categories = categoryDAO.getAllCategories();
            List<Priority> priorities = priorityDAO.getAllPriorities();
            request.setAttribute("categories", categories);
            request.setAttribute("priorities", priorities);
            request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
        }
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDateStr = request.getParameter("due_date");
        int priorityId = Integer.parseInt(request.getParameter("priority"));
        int categoryId = Integer.parseInt(request.getParameter("category"));
        boolean isCompleted = Boolean.parseBoolean(request.getParameter("is_completed"));

        if (!ValidationUtils.isValidTaskTitle(title) || !ValidationUtils.isValidDueDate(dueDateStr)) {
            request.setAttribute("errorMessage", "Dati attività non validi. Titolo obbligatorio, data nel formato YYYY-MM-DD.");
            List<Category> categories = categoryDAO.getAllCategories();
            List<Priority> priorities = priorityDAO.getAllPriorities();
            request.setAttribute("categories", categories);
            request.setAttribute("priorities", priorities);
            Task currentTask = taskDAO.getTaskById(taskId, userId); // Ricarica la task per mostrare il form con i dati attuali
            request.setAttribute("task", currentTask);
            request.getRequestDispatcher("/WEB-INF/views/edit_task.jsp").forward(request, response);
            return;
        }

        try {
            Task taskToUpdate = new Task();
            taskToUpdate.setId(taskId);
            taskToUpdate.setUserId(userId); // Assicurati che l'utente sia autorizzato
            taskToUpdate.setTitle(title);
            taskToUpdate.setDescription(description);
            taskToUpdate.setDueDate(Date.valueOf(dueDateStr));
            taskToUpdate.setPriorityId(priorityId);
            taskToUpdate.setCategoryId(categoryId);
            taskToUpdate.setCompleted(isCompleted);

            taskDAO.updateTask(taskToUpdate);
            response.sendRedirect(request.getContextPath() + "/app/tasks?message=taskUpdated");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante l'aggiornamento dell'attività: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/app/tasks/edit?id=" + taskId + "&errorMessage=" + e.getMessage());
        }
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("id"));
        try {
            taskDAO.deleteTask(taskId, userId); // Assicurati che l'utente sia autorizzato a eliminare questa task
            response.sendRedirect(request.getContextPath() + "/app/tasks?message=taskDeleted");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante l'eliminazione dell'attività: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/app/tasks?errorMessage=" + e.getMessage());
        }
    }

    private void toggleTaskCompletion(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("id"));
        boolean isCompleted = Boolean.parseBoolean(request.getParameter("is_completed")); // Valore passato dal form

        try {
            Task task = taskDAO.getTaskById(taskId, userId);
            if (task != null) {
                task.setCompleted(isCompleted);
                taskDAO.updateTaskCompletionStatus(taskId, isCompleted);
                response.sendRedirect(request.getContextPath() + "/app/tasks?message=taskStatusUpdated");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task non trovata o non autorizzato.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante l'aggiornamento dello stato dell'attività: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/app/tasks?errorMessage=" + e.getMessage());
        }
    }
}