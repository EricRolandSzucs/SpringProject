package edu.bbte.idde.seim1964.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.seim1964.backend.dao.AbstractDaoFactory;
import edu.bbte.idde.seim1964.backend.dao.UserDao;
import edu.bbte.idde.seim1964.backend.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/count")
public class CounterServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServlet.class);
    private UserDao userDao;
    private ObjectMapper objectMapper;


    @Override
    public void init() throws ServletException {
        LOGGER.info("Servlet initialized");
        userDao = AbstractDaoFactory.getDaoFactory().getUserDao();
        objectMapper = ObjectMapperFactory.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer irasi = userDao.findIrasi();
            Integer olvasasi = userDao.findOlvasasi();

            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), "Olvasasi: " + olvasasi + " Irasi: " + irasi);

        } catch (NumberFormatException exc) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Wrong ID format.");
        }

    }
}
