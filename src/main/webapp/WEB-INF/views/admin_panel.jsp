<%-- /WEB-INF/views/admin_panel.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tasktracker.model.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaskTracker - Pannello Amministratore</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="navbar.jsp" />

    <div class="container mt-4">
        <h2 class="mb-4">Pannello Amministratore</h2>

        <% String message = request.getParameter("message"); %>
        <% if (message != null) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <% if ("userStatusUpdated".equals(message)) { %>
                    Stato utente aggiornato con successo!
                <% } else if ("passwordReset".equals(message)) {
                    String resetUser = request.getParameter("resetUser");
                    String tempPassword = (String) request.getSession().getAttribute("tempPassword");
                    request.getSession().removeAttribute("tempPassword"); // Rimuovi dalla sessione dopo l'uso
                %>
                    Password per l'utente ID <%= resetUser %> resettata con successo! Nuova password temporanea: <strong><%= tempPassword %></strong> (Comunica all'utente di cambiarla)
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

        <h3>Elenco Utenti Registrati</h3>
        <table class="table table-striped table-hover mt-3">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Admin</th>
                    <th>Stato</th>
                    <th>Azioni</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<User> users = (List<User>) request.getAttribute("users");
                    if (users != null && !users.isEmpty()) {
                        for (User user : users) {
                %>
                            <tr>
                                <td><%= user.getId() %></td>
                                <td><%= user.getUsername() %></td>
                                <td><%= user.getEmail() != null ? user.getEmail() : "N/A" %></td>
                                <td><%= user.isAdmin() ? "Sì" : "No" %></td>
                                <td><%= user.isActive() ? "Attivo" : "Disabilitato" %></td>
                                <td>
                                    <form action="<%= request.getContextPath() %>/app/admin" method="post" class="d-inline me-2">
                                        <input type="hidden" name="action" value="toggle_status">
                                        <input type="hidden" name="userId" value="<%= user.getId() %>">
                                        <input type="hidden" name="currentStatus" value="<%= user.isActive() %>">
                                        <button type="submit" class="btn btn-sm <%= user.isActive() ? "btn-warning" : "btn-success" %>">
                                            <%= user.isActive() ? "Disabilita" : "Abilita" %>
                                        </button>
                                    </form>
                                    <form action="<%= request.getContextPath() %>/app/admin" method="post" class="d-inline">
                                        <input type="hidden" name="action" value="reset_password">
                                        <input type="hidden" name="userId" value="<%= user.getId() %>">
                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Sei sicuro di voler resettare la password di <%= user.getUsername() %>?');">Reset Password</button>
                                    </form>
                                </td>
                            </tr>
                <%
                        }
                    } else {
                %>
                        <tr>
                            <td colspan="6" class="text-center">Nessun utente registrato.</td>
                        </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>