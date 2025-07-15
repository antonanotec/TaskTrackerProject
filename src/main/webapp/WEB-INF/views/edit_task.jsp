<%-- /WEB-INF/views/edit_task.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tasktracker.model.Task" %>
<%@ page import="com.tasktracker.model.Category" %>
<%@ page import="com.tasktracker.model.Priority" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<% SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaskTracker - Modifica Attività</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="navbar.jsp" />

    <div class="container mt-4">
        <h2 class="mb-4">Modifica Attività</h2>

        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= errorMessage %>
            </div>
        <% } %>

        <% Task task = (Task) request.getAttribute("task"); %>
        <% if (task != null) { %>
            <form action="<%= request.getContextPath() %>/app/tasks/edit" method="post">
                <input type="hidden" name="id" value="<%= task.getId() %>">
                <div class="mb-3">
                    <label for="title" class="form-label">Titolo Attività <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="title" name="title" value="<%= task.getTitle() %>" required maxlength="100">
                </div>
                <div class="mb-3">
                    <label for="description" class="form-label">Descrizione</label>
                    <textarea class="form-control" id="description" name="description" rows="3" maxlength="500"><%= task.getDescription() != null ? task.getDescription() : "" %></textarea>
                </div>
                <div class="mb-3">
                    <label for="due_date" class="form-label">Data di Scadenza</label>
                    <input type="date" class="form-control" id="due_date" name="due_date" value="<%= task.getDueDate() != null ? sdf.format(task.getDueDate()) : "" %>">
                </div>
                <div class="mb-3">
                    <label for="priority" class="form-label">Priorità <span class="text-danger">*</span></label>
                    <select class="form-select" id="priority" name="priority" required>
                        <%
                            List<Priority> priorities = (List<Priority>) request.getAttribute("priorities");
                            if (priorities != null) {
                                for (Priority p : priorities) {
                        %>
                                    <option value="<%= p.getId() %>" <%= task.getPriorityId() == p.getId() ? "selected" : "" %>><%= p.getName() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="category" class="form-label">Categoria <span class="text-danger">*</span></label>
                    <select class="form-select" id="category" name="category" required>
                         <%
                            List<Category> categories = (List<Category>) request.getAttribute("categories");
                            if (categories != null) {
                                for (Category c : categories) {
                        %>
                                    <option value="<%= c.getId() %>" <%= task.getCategoryId() == c.getId() ? "selected" : "" %>><%= c.getName() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="is_completed" name="is_completed" value="true" <%= task.isCompleted() ? "checked" : "" %>>
                    <label class="form-check-label" for="is_completed">Completata</label>
                </div>
                <button type="submit" class="btn btn-primary">Aggiorna Attività</button>
                <a href="<%= request.getContextPath() %>/app/tasks" class="btn btn-secondary">Annulla</a>
            </form>
        <% } else { %>
            <div class="alert alert-warning" role="alert">
                Attività non trovata.
            </div>
        <% } %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>