// AdminServlet.java
package com.tasktracker.servlet;

import com.tasktracker.dao.UserDAO;
import com.tasktracker.model.User;
import com.tasktracker.util.PasswordUtils;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/app/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !(boolean)session.getAttribute("isAdmin")) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
            return;
        }

        try {
            List<User> users = userDAO.getAllUsers();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/views/admin_panel.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante il recupero degli utenti: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !(boolean)session.getAttribute("isAdmin")) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        int userId = Integer.parseInt(request.getParameter("userId"));

        try {
            switch (action) {
                case "toggle_status":
                    boolean currentStatus = Boolean.parseBoolean(request.getParameter("currentStatus"));
                    userDAO.updateUserStatus(userId, !currentStatus);
                    response.sendRedirect(request.getContextPath() + "/app/admin?message=userStatusUpdated");
                    break;
                case "reset_password":
                    String newTempPassword = generateTemporaryPassword();
                    userDAO.updateUserPassword(userId, PasswordUtils.hashPassword(newTempPassword));
                    request.getSession().setAttribute("tempPassword", newTempPassword);
                    response.sendRedirect(request.getContextPath() + "/app/admin?message=passwordReset&resetUser=" + userId);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Azione non riconosciuta.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore nell'azione amministrativa: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/app/admin?errorMessage=" + e.getMessage());
        }
    }

    private String generateTemporaryPassword() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
