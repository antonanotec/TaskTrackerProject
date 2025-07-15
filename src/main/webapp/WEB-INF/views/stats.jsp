<%-- /WEB-INF/views/stats.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaskTracker - Progressi e Statistiche</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script> <style>
        .chart-container {
            width: 80%;
            margin: auto;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
    <jsp:include page="navbar.jsp" />

    <div class="container mt-4">
        <h2 class="mb-4">I Tuoi Progressi</h2>

        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= errorMessage %>
            </div>
        <% } %>

        <%
            Map<String, Integer> completedTasksToday = (Map<String, Integer>) request.getAttribute("completedTasksToday");
            Map<String, Integer> completedTasksThisWeek = (Map<String, Integer>) request.getAttribute("completedTasksThisWeek");
            Map<String, Integer> tasksByCategory = (Map<String, Integer>) request.getAttribute("tasksByCategory");
            Map<String, Integer> tasksByPriority = (Map<String, Integer>) request.getAttribute("tasksByPriority");
        %>

        <div class="row mb-4">
            <div class="col-md-6">
                <div class="card text-center">
                    <div class="card-body">
                        <h5 class="card-title">Completate Oggi</h5>
                        <p class="card-text fs-1 fw-bold"><%= completedTasksToday != null ? completedTasksToday.getOrDefault("today", 0) : 0 %></p>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card text-center">
                    <div class="card-body">
                        <h5 class="card-title">Completate Questa Settimana</h5>
                        <p class="card-text fs-1 fw-bold"><%= completedTasksThisWeek != null ? completedTasksThisWeek.getOrDefault("thisWeek", 0) : 0 %></p>
                    </div>
                </div>
            </div>
        </div>

        <h3>Distribuzione Attività</h3>
        <% if (tasksByCategory == null || tasksByCategory.isEmpty() && tasksByPriority == null || tasksByPriority.isEmpty()) { %>
            <div class="alert alert-info text-center" role="alert">
                [cite_start]Nessuna attività completata per le statistiche. [cite: 162]
            </div>
        <% } else { %>
            <div class="row">
                <div class="col-md-6">
                    <div class="chart-container">
                        <canvas id="categoryChart"></canvas>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="chart-container">
                        <canvas id="priorityChart"></canvas>
                    </div>
                </div>
            </div>
        <% } %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Funzione per generare colori casuali
        function getRandomColor() {
            var letters = '0123456789ABCDEF';
            var color = '#';
            for (var i = 0; i < 6; i++) {
                color += letters[Math.floor(Math.random() * 16)];
            }
            return color;
        }

        // Dati per i grafici
        var tasksByCategory = {
            labels: [
                <% if (tasksByCategory != null) {
                    Set<String> categories = tasksByCategory.keySet();
                    int i = 0;
                    for (String category : categories) { %>
                        '<%= category %>'<%= (i < categories.size() - 1) ? "," : "" %>
                    <% i++; }
                } %>
            ],
            data: [
                <% if (tasksByCategory != null) {
                    Set<String> categories = tasksByCategory.keySet();
                    int i = 0;
                    for (String category : categories) { %>
                        <%= tasksByCategory.get(category) %><%= (i < categories.size() - 1) ? "," : "" %>
                    <% i++; }
                } %>
            ]
        };

        var tasksByPriority = {
            labels: [
                <% if (tasksByPriority != null) {
                    Set<String> priorities = tasksByPriority.keySet();
                    int i = 0;
                    for (String priority : priorities) { %>
                        '<%= priority %>'<%= (i < priorities.size() - 1) ? "," : "" %>
                    <% i++; }
                } %>
            ],
            data: [
                <% if (tasksByPriority != null) {
                    Set<String> priorities = tasksByPriority.keySet();
                    int i = 0;
                    for (String priority : priorities) { %>
                        <%= tasksByPriority.get(priority) %><%= (i < priorities.size() - 1) ? "," : "" %>
                    <% i++; }
                } %>
            ]
        };

        // Grafico per Categorie
        if (document.getElementById('categoryChart')) {
            var ctxCategory = document.getElementById('categoryChart').getContext('2d');
            new Chart(ctxCategory, {
                type: 'pie', // Puoi scegliere 'bar', 'doughnut', ecc.
                data: {
                    labels: tasksByCategory.labels,
                    datasets: [{
                        label: 'Attività per Categoria',
                        data: tasksByCategory.data,
                        backgroundColor: tasksByCategory.labels.map(() => getRandomColor()), // Colori casuali per ogni fetta
                        borderColor: '#fff',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: 'Attività per Categoria'
                        }
                    }
                }
            });
        }

        // Grafico per Priorità
        if (document.getElementById('priorityChart')) {
            var ctxPriority = document.getElementById('priorityChart').getContext('2d');
            new Chart(ctxPriority, {
                type: 'doughnut', // Puoi scegliere 'bar', 'pie', ecc.
                data: {
                    labels: tasksByPriority.labels,
                    datasets: [{
                        label: 'Attività per Priorità',
                        data: tasksByPriority.data,
                        backgroundColor: tasksByPriority.labels.map(() => getRandomColor()), // Colori casuali per ogni fetta
                        borderColor: '#fff',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: 'Attività per Priorità'
                        }
                    }
                }
            });
        }
    </script>
</body>
</html>