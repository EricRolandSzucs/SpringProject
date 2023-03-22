package edu.bbte.idde.seim1964.spring.dao;

import edu.bbte.idde.seim1964.spring.model.Announcement;

import java.util.Collection;

public interface AnnouncementDao extends Dao<Announcement> {
    Collection<Announcement> findByTitle(String title);

    Long findCarId(Long id);
}
