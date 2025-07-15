// UserServlet.java
package com.tasktracker.servlet;

import com.tasktracker.dao.UserDAO;
import com.tasktracker.model.User;
import com.tasktracker.util.PasswordUtils;
import com.tasktracker.util.ValidationUtils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/login", "/register", "/logout"})
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/login".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } else if ("/register".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        } else if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login?message=logoutSuccess");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/register".equals(path)) {
            registerUser(request, response);
        } else if ("/login".equals(path)) {
            authenticateUser(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        if (!ValidationUtils.isValidUsername(username) || !ValidationUtils.isValidPassword(password) || !ValidationUtils.isValidEmail(email)) {
            request.setAttribute("errorMessage", "Dati di registrazione non validi. Username (3-20 alfanumerici), password (almeno 6 caratteri), email valida.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        if (userDAO.getUserByUsername(username) != null) {
            request.setAttribute("errorMessage", "Username già in uso. Scegli un username diverso.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        try {
            String hashedPassword = PasswordUtils.hashPassword(password);
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setHashedPassword(hashedPassword);
            newUser.setEmail(email);
            newUser.setAdmin(false);
            newUser.setActive(true);

            userDAO.addUser(newUser);

            response.sendRedirect(request.getContextPath() + "/login?message=registrationSuccess");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante la registrazione: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }

    private void authenticateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userDAO.getUserByUsername(username);

            if (user != null && PasswordUtils.checkPassword(password, user.getHashedPassword())) {
                if (!user.isActive()) {
                    request.setAttribute("errorMessage", "Il tuo account è stato disabilitato. Contatta l'amministratore.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
                }

                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("isAdmin", user.isAdmin());

                response.sendRedirect(request.getContextPath() + "/app/dashboard");
            } else {
                request.setAttribute("errorMessage", "Credenziali non valide. Riprova.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore tecnico durante l'autenticazione: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
