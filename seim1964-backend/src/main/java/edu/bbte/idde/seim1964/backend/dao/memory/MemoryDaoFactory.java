package edu.bbte.idde.seim1964.backend.dao.memory;

import edu.bbte.idde.seim1964.backend.dao.AnnouncementDao;
import edu.bbte.idde.seim1964.backend.dao.DaoFactory;
import edu.bbte.idde.seim1964.backend.dao.UserDao;

public class MemoryDaoFactory implements DaoFactory {
    private AnnouncementDao announcementDao;
    private UserDao userDao;

    @Override
    public AnnouncementDao getAnnouncementDao() {
        if (announcementDao == null) {
            announcementDao = new AnnouncementMemory();
        }

        return announcementDao;
    }

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserMemory();
        }

        return userDao;
    }
}