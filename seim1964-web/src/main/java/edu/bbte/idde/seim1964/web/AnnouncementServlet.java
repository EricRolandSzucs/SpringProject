package edu.bbte.idde.seim1964.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.seim1964.backend.dao.AnnouncementDao;
import edu.bbte.idde.seim1964.backend.dao.memory.AnnouncementMemory;
import edu.bbte.idde.seim1964.backend.model.Announcement;
import edu.bbte.idde.seim1964.backend.model.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@WebServlet("/announcements")
public class AnnouncementServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementServlet.class);
    private AnnouncementDao announcementDao;
    private ObjectMapper objectMapper;

    public Boolean carCheck(Car car) {
        return car.getBrand() != null && car.getModel() != null && car.getPrice()
                != null && car.getType() != null && car.getColor() != null;
    }

    public Boolean inputCheck(Announcement announ) {
        return announ.getTitle() != null && announ.getUserId() != null && announ.getDate() != null
                && announ.getDescription() != null && carCheck(announ.getCar());
    }

    @Override
    public void init() throws ServletException {
        LOGGER.info("Servlet initialized");
        announcementDao = new AnnouncementMemory();
        objectMapper = ObjectMapperFactory.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("GET /announcements");
        String idToGet = req.getParameter("id");
        if (idToGet == null) {
            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), announcementDao.findAll());
        } else {
            try {
                Long id = Long.parseLong(idToGet);
                Announcement announcement = announcementDao.findById(id);
                if (announcement == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println("Announcement not found.");
                } else {
                    resp.setHeader("Content-Type", "application/json");
                    objectMapper.writeValue(resp.getOutputStream(), announcement);
                }
            } catch (NumberFormatException exc) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Wrong ID format.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("POST /announcements");
        try {
            Announcement announcement = objectMapper.readValue(req.getInputStream(), Announcement.class);

            if (!inputCheck(announcement)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Missing announcement details.");
                return;
            }
            announcementDao.create(announcement);
            LOGGER.info("Announcement successfully created.");
            resp.setHeader("Content-Type", "application/json");
            objectMapper.writeValue(resp.getOutputStream(), announcementDao.findAll());

        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Body is incomplete or some elements are the wrong type.");
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("DELETE /announcements");

        String idToDelete = req.getParameter("id");

        if (idToDelete == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid ID");
        } else {
            try {
                Long id = Long.parseLong(idToDelete);
                announcementDao.delete(id);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("Announcement was deleted.");

            } catch (NumberFormatException exc) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Invalid ID");
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("PUT /announcements");

        String idToUpdate = req.getParameter("id");

        if (idToUpdate == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Invalid ID");
        } else {
            try {
                Long id = Long.parseLong(idToUpdate);
                Announcement announcement = announcementDao.findById(id);
                if (announcement == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println("Announcement not found");
                } else {
                    try {
                        Announcement announ = objectMapper.readValue(req.getInputStream(), Announcement.class);

                        if (!inputCheck(announ)) {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().println("Missing announcement details.");
                            return;
                        }

                        announcementDao.update(id, announ);
                        LOGGER.info("Announcement successfully update.");
                        resp.setHeader("Content-Type", "application/json");
                        announcement = announcementDao.findById(id);
                        objectMapper.writeValue(resp.getOutputStream(), announcement);
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
