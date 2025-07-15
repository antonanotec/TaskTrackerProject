<%-- /WEB-INF/views/dashboard.jsp --%>
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
    <title>TaskTracker - Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f7f6; }
        .task-card {
            border-left: 5px solid;
            margin-bottom: 15px;
        }
        .priority-Alta { border-color: #dc3545; } /* Rosso */
        .priority-Media { border-color: #ffc107; } /* Giallo */
        .priority-Bassa { border-color: #17a2b8; } /* Blu */
        .task-completed {
            text-decoration: line-through;
            opacity: 0.7;
        }
    </style>
</head>
<body>
    <jsp:include page="navbar.jsp" />

    <div class="container mt-4">
        <h2 class="mb-4">Le Tue Attività</h2>

        <% String message = request.getParameter("message"); %>
        <% if (message != null) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <% if ("taskCreated".equals(message)) { %>
                    Attività creata con successo!
                <% } else if ("taskUpdated".equals(message)) { %>
                    Attività aggiornata con successo!
                <% } else if ("taskDeleted".equals(message)) { %>
                    Attività eliminata con successo!
                <% } else if ("taskStatusUpdated".equals(message)) { %>
                    Stato attività aggiornato!
                <% } %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= errorMessage %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>

        <div class="d-flex justify-content-between mb-4">
            <a href="<%= request.getContextPath() %>/app/tasks/create" class="btn btn-primary">
                <i class="fas fa-plus-circle"></i> Nuova Attività
            </a>
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="filterDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                    Filtra Attività
                </button>
                <ul class="dropdown-menu" aria-labelledby="filterDropdown">
                    <li><a class="dropdown-item" href="<%= request.getContextPath() %>/app/tasks">Tutte le Attività</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><h6 class="dropdown-header">Per Priorità</h6></li>
                    <%
                        List<Priority> priorities = (List<Priority>) request.getAttribute("priorities");
                        if (priorities != null) {
                            for (Priority p : priorities) {
                    %>
                                <li><a class="dropdown-item" href="<%= request.getContextPath() %>/app/tasks?priority=<%= p.getId() %>"><%= p.getName() %></a></li>
                    <%
                            }
                        }
                    %>
                    <li><hr class="dropdown-divider"></li>
                    <li><h6 class="dropdown-header">Per Categoria</h6></li>
                    <%
                        List<Category> categories = (List<Category>) request.getAttribute("categories");
                        if (categories != null) {
                            for (Category c : categories) {
                    %>
                                <li><a class="dropdown-item" href="<%= request.getContextPath() %>/app/tasks?category=<%= c.getId() %>"><%= c.getName() %></a></li>
                    <%
                            }
                        }
                    %>
                    <li><hr class="dropdown-divider"></li>
                    <li><h6 class="dropdown-header">Per Stato</h6></li>
                    <li><a class="dropdown-item" href="<%= request.getContextPath() %>/app/tasks?completed=false">In Sospeso</a></li>
                    <li><a class="dropdown-item" href="<%= request.getContextPath() %>/app/tasks?completed=true">Completate</a></li>
                </ul>
            </div>
        </div>

        <div class="row">
            <%
                List<Task> tasks = (List<Task>) request.getAttribute("tasks");
                if (tasks != null && !tasks.isEmpty()) {
                    for (Task task : tasks) {
            %>
                        <div class="col-md-6">
                            <div class="card task-card priority-<%= task.getPriorityName() %> <%= task.isCompleted() ? "task-completed" : "" %>">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h5 class="card-title mb-0"><%= task.getTitle() %></h5>
                                        <form action="<%= request.getContextPath() %>/app/tasks/complete" method="post" class="d-inline">
                                            <input type="hidden" name="id" value="<%= task.getId() %>">
                                            <input type="hidden" name="is_completed" value="<%= !task.isCompleted() %>">
                                            <button type="submit" class="btn btn-sm <%= task.isCompleted() ? "btn-warning" : "btn-success" %>">
                                                <%= task.isCompleted() ? "Segna come NON Completata" : "Segna come Completata" %>
                                            </button>
                                        </form>
                                    </div>
                                    <p class="card-text"><%= task.getDescription() != null ? task.getDescription() : "Nessuna descrizione" %></p>
                                    <p class="card-text"><small class="text-muted">Scadenza: <%= task.getDueDate() != null ? sdf.format(task.getDueDate()) : "N/A" %></small></p>
                                    <p class="card-text"><small class="text-muted">Priorità: <%= task.getPriorityName() %></small></p>
                                    <p class="card-text"><small class="text-muted">Categoria: <%= task.getCategoryName() %></small></p>
                                    <div class="d-flex justify-content-end">
                                        <a href="<%= request.getContextPath() %>/app/tasks/edit?id=<%= task.getId() %>" class="btn btn-sm btn-info me-2">Modifica</a>
                                        <form action="<%= request.getContextPath() %>/app/tasks/delete" method="post" class="d-inline">
                                            <input type="hidden" name="id" value="<%= task.getId() %>">
                                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Sei sicuro di voler eliminare questa attività?');">Elimina</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
            <%
                    }
                } else {
            %>
                    <div class="col-12">
                        <div class="alert alert-info text-center" role="alert">
                            Nessuna attività trovata. Prova a crearne una!
                        </div>
                    </div>
            <%
                }
            %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://kit.fontawesome.com/your-font-awesome-kit-id.js" crossorigin="anonymous"></script> </body>
</html>