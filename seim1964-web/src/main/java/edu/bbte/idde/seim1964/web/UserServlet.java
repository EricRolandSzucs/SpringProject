package edu.bbte.idde.seim1964.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.seim1964.backend.dao.AbstractDaoFactory;
import edu.bbte.idde.seim1964.backend.dao.UserDao;
import edu.bbte.idde.seim1964.backend.dao.memory.UserMemory;
import edu.bbte.idde.seim1964.backend.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServlet.class);
    private UserDao userDao;
    private ObjectMapper objectMapper;

    public Boolean inputCheck(User user) {
        return user.getUsername() != null && user.getEmail() != null && user.getName() != null
                && user.getPassword() != null && user.getPhone() != null;
    }

    @Override
    public void init() throws ServletException {
        LOGGER.info("Servlet initialized");
        userDao = AbstractDaoFactory.getDaoFactory().getUserDao();
        objectMapper = ObjectMapperFactory.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("GET /users");
        String idToGet = req.getParameter("id");
        if (idToGet == null) {
            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), userDao.findAll());
        } else {
            try {
                Long id = Long.parseLong(idToGet);
                User user = userDao.findById(id);
                if (user == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println("User not found.");
                } else {
                    resp.setHeader("Content-Type", "application/json");
                    objectMapper.writeValue(resp.getOutputStream(), user);
                }
            } catch (NumberFormatException exc) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Wrong ID format.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("POST /users");
        try {
            User user = objectMapper.readValue(req.getInputStream(), User.class);

            if (!inputCheck(user)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Missing user details.");
                return;
            }
            userDao.create(user);
            LOGGER.info("User successfully created.");
            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), userDao.findAll());

        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Body is incomplete or some elements are the wrong type.");
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("DELETE /users");

        String idToDelete = req.getParameter("id");

        if (idToDelete == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid ID");
        } else {
            try {
                Long id = Long.parseLong(idToDelete);
                userDao.delete(id);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("User was deleted.");

            } catch (NumberFormatException exc) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Invalid ID");
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("PUT /users");

        String idToUpdate = req.getParameter("id");

        if (idToUpdate == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid ID");
        } else {
            try {
                Long id = Long.parseLong(idToUpdate);
                User user = userDao.findById(id);
                if (user == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println("User not found");
                } else {
                    try {
                        User user1 = objectMapper.readValue(req.getInputStream(), User.class);

                        if (!inputCheck(user1)) {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().println("Missing user details.");
                            return;
                        }

                        userDao.update(id, user1);
                        LOGGER.info("User successfully update.");
                        resp.setHeader("Content-Type", "application/json");
                        user = userDao.findById(id);
                        objectMapper.writeValue(resp.getOutputStream(), user);
                    } catch (IOException e) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().println("Body is incomplete or some elements are the wrong type.\n" + e);
                    }
                }

            } catch (NumberFormatException exc) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Invalid ID");
            }
        }

    }
}
