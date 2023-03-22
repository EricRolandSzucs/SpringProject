package edu.bbte.idde.seim1964.backend.dao.jdbc;

import edu.bbte.idde.seim1964.backend.dao.AnnouncementDao;
import edu.bbte.idde.seim1964.backend.dao.DaoFactory;
import edu.bbte.idde.seim1964.backend.dao.UserDao;

public class JdbcDaoFactory implements DaoFactory {
    private AnnouncementJdbcDao announcementJdbcDao;
    private UserJdbcDao userJdbcDao;

    public JdbcDaoFactory() {
        new TablesDao();
    }

    @Override
    public AnnouncementDao getAnnouncementDao() {
        if (announcementJdbcDao == null) {
            announcementJdbcDao = new AnnouncementJdbcDao();
        }

        return announcementJdbcDao;
    }

    @Override
    public UserDao getUserDao() {
        if (userJdbcDao == null) {
            userJdbcDao = new UserJdbcDao();
        }

        return userJdbcDao;
    }
}
