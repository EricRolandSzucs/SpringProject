package edu.bbte.idde.seim1964.desktop;

import edu.bbte.idde.seim1964.backend.dao.AbstractDaoFactory;
import edu.bbte.idde.seim1964.backend.dao.AnnouncementDao;
import edu.bbte.idde.seim1964.backend.dao.DaoFactory;
import edu.bbte.idde.seim1964.backend.dao.UserDao;
import edu.bbte.idde.seim1964.backend.model.Announcement;
import edu.bbte.idde.seim1964.backend.model.Car;
import edu.bbte.idde.seim1964.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DaoFactory daoFactory = AbstractDaoFactory.getDaoFactory();

        UserDao userDao = daoFactory.getUserDao();

        User user1 = new User("test2", "Test", "test@gmail.com", "test", "0723237362");
        User user2 = new User("test3", "Test", "test@gmail.com", "test", "0723237362");

        // CREATE NEW USER
        userDao.create(user1);
        userDao.create(user2);
        LOG.info(userDao.findAll().toString() + '\n');

        // UPDATE USER
        userDao.update(1L, new User("lemon", "Lemon", "lemon@gmail.com", "lemon", "0723337362"));
        LOG.info(userDao.findAll().toString() + '\n');

        // FIND A USER BY ID
        LOG.info(userDao.findById(1L).toString() + '\n');

        // FIND BY USERNAME
        LOG.info(userDao.findByUsername("lemon").toString() + '\n');

        // DELETE USER
        userDao.delete(2L);

        LOG.info(userDao.findAll().toString() + '\n');

        // ---------------------------------------------------------------------
        Car car = new Car("Bull", "Mod", 12, "Benz", "Sarga");
        LocalDate date = LocalDate.of(2018, 3, 14);
        Announcement announce = new Announcement("Rescue", 1L, date, car, ":)");

        AnnouncementDao announcementDao = daoFactory.getAnnouncementDao();

        // CREATE NEW ANNOUNCEMENT
        announcementDao.create(announce);
        announcementDao.create(announce);
        LOG.info(announcementDao.findAll().toString() + '\n');

        // UPDATE ANNOUNCEMENT
        announcementDao.update(1L, new Announcement("BMW", 1L, date, car, "Vegy"));
        LOG.info(announcementDao.findAll().toString() + '\n');

        // FIND AN ANNOUNCEMENT BY ID
        LOG.info(announcementDao.findById(1L).toString() + '\n');

        // FIND BY TITLE
        LOG.info(announcementDao.findByTitle("BMW").toString() + '\n');

        // DELETE ANNOUNCEMENT
        announcementDao.delete(1L);

        LOG.info(announcementDao.findAll().toString() + '\n');
    }
}
