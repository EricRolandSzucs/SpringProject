package edu.bbte.idde.seim1964.backend.dao;

public interface DaoFactory {
    AnnouncementDao getAnnouncementDao();

    UserDao getUserDao();
}
