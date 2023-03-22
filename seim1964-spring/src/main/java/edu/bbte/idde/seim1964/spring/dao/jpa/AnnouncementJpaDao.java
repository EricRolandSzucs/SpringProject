package edu.bbte.idde.seim1964.spring.dao.jpa;

import edu.bbte.idde.seim1964.spring.dao.AnnouncementDao;
import edu.bbte.idde.seim1964.spring.model.Announcement;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface AnnouncementJpaDao extends JpaRepository<Announcement, Long>, AnnouncementDao {
    @Override
    @Query(value = "SELECT car_id FROM announcements Where id = :id", nativeQuery = true)
    Long findCarId(Long id);
}
