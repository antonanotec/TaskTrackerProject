// authentication_filter.java
package com.tasktracker.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro, se necessaria
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Escludi le pagine di login e registrazione dal filtro
        if (path.startsWith(contextPath + "/login") || path.startsWith(contextPath + "/register")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false); // Non creare una nuova sessione se non esiste

        if (session == null || session.getAttribute("userId") == null) {
            [cite_start]// L'utente non è autenticato, reindirizza alla pagina di login [cite: 124]
            httpResponse.sendRedirect(contextPath + "/login?error=sessionExpired");
            return;
        }

        // L'utente è autenticato, continua la catena dei filtri/servlet
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Pulizia del filtro, se necessaria
    }
}