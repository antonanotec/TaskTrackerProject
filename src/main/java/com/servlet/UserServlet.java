// user_servlet.java
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

@WebServlet({"/login", "/register", "/logout"}) // Mappatura per login, register e logout
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
                session.invalidate(); // Invalida la sessione
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

        [cite_start]// Validazione dei dati [cite: 107]
        if (!ValidationUtils.isValidUsername(username) || !ValidationUtils.isValidPassword(password) || !ValidationUtils.isValidEmail(email)) {
            request.setAttribute("errorMessage", "Dati di registrazione non validi. Username almeno 3 caratteri, password almeno 6, email valida.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        [cite_start]// Verifica unicità username [cite: 108]
        if (userDAO.getUserByUsername(username) != null) {
            request.setAttribute("errorMessage", "Username già in uso. Scegli un username diverso.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        try {
            String hashedPassword = PasswordUtils.hashPassword(password); [cite_start]// Cripta la password [cite: 109]
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setHashedPassword(hashedPassword);
            newUser.setEmail(email);
            newUser.setAdmin(false); // Default non amministratore

            userDAO.addUser(newUser); [cite_start]// Salva l'utente nel database [cite: 110]

            response.sendRedirect(request.getContextPath() + "/login?message=registrationSuccess"); [cite_start]// Reindirizza al login [cite: 112]
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante la registrazione: " + e.getMessage()); [cite_start]// Messaggio di errore tecnico [cite: 116]
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }

    private void authenticateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userDAO.getUserByUsername(username);

            [cite_start]// Validazione credenziali e hash della password [cite: 121, 122]
            if (user != null && PasswordUtils.checkPassword(password, user.getHashedPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("isAdmin", user.isAdmin()); // Memorizza se è admin

                response.sendRedirect(request.getContextPath() + "/app/dashboard"); [cite_start]// Reindirizza alla dashboard [cite: 124]
            } else {
                request.setAttribute("errorMessage", "Credenziali non valide. Riprova."); [cite_start]// Errore di autenticazione [cite: 126]
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore tecnico durante l'autenticazione: " + e.getMessage()); [cite_start]// Errore tecnico [cite: 127]
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}