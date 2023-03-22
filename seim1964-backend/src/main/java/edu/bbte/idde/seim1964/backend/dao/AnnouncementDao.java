package edu.bbte.idde.seim1964.backend.dao;

import edu.bbte.idde.seim1964.backend.model.Announcement;

import java.util.Collection;

public interface AnnouncementDao extends Dao<Announcement> {
    Collection<Announcement> findByTitle(String title);
}
