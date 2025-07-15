<%-- /WEB-INF/views/navbar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/app/dashboard">TaskTracker</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/app/dashboard">Dashboard</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/app/tasks/create">Crea Attività</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/app/stats">Statistiche</a>
                </li>
                <% Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
                   if (isAdmin != null && isAdmin) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/app/admin">Admin Panel</a>
                    </li>
                <% } %>
            </ul>
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <span class="nav-link text-white">Ciao, <%= session.getAttribute("username") %>!</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link btn btn-outline-light btn-sm" href="<%= request.getContextPath() %>/logout">Logout</a>
                </li>
            </ul>
        </div>
    </div>
</nav>