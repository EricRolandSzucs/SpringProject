package edu.bbte.idde.seim1964.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

@WebFilter("/index")
public class LoginFilter extends HttpFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);
    private static final String USER = "admin";
    private static final String PASS = "admin";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = req.getSession();
        if (null != session.getAttribute("username")) {
            LOGGER.info("User logged in");
            chain.doFilter(req, res);
            return;
        }
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (Objects.equals(USER, username) && Objects.equals(PASS, password)) {
            session.setAttribute("username", username);
            chain.doFilter(req, res);
        } else {
            req.getRequestDispatcher("login.html").forward(req, res);
        }

    }
}
