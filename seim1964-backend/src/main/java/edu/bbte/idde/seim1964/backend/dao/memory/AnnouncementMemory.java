package edu.bbte.idde.seim1964.backend.dao.memory;

import edu.bbte.idde.seim1964.backend.dao.AnnouncementDao;
import edu.bbte.idde.seim1964.backend.model.Announcement;
import edu.bbte.idde.seim1964.backend.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AnnouncementMemory implements AnnouncementDao {

    private static final Map<Long, Announcement> ENTITIES = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final Logger LOG = LoggerFactory.getLogger(AnnouncementMemory.class);

    static {

        Car car = new Car("BMW", "F40", 29630, "Petrol", "White");
        LocalDate date = LocalDate.of(2017, 1, 13);
        Announcement announce1 = new Announcement("Hot BMW", 1L, date, car, "Majdnem okay.");
        Announcement announce2 = new Announcement("Cold BMW", 1L, date, car, "Okay.");
        Long id1 = ID_GENERATOR.getAndIncrement();
        Long id2 = ID_GENERATOR.getAndIncrement();
        announce1.setId(id1);
        announce2.setId(id2);
        ENTITIES.put(id1, announce1);
        ENTITIES.put(id2, announce2);
    }

    @Override
    public Announcement findById(Long id) {
        return ENTITIES.get(id);
    }

    @Override
    public Announcement create(Announcement announcement) {
        Long id = ID_GENERATOR.getAndIncrement();
        announcement.setId(id);
        return ENTITIES.put(id, announcement);
    }

    @Override
    public Collection<Announcement> findAll() {
        return ENTITIES.values();
    }

    @Override
    public Announcement update(Long id, Announcement newAnnouncement) {
        if (ENTITIES.remove(id) == null) {
            LOG.warn("Announcement ID doesn't exist");
        }
        newAnnouncement.setId(id);
        return ENTITIES.put(id, newAnnouncement);
    }

    @Override
    public void delete(Long id) {
        if (ENTITIES.remove(id) == null) {
            LOG.warn("Announcement ID doesn't exist");
        }
    }

    @Override
    public Collection<Announcement> findByTitle(String title) {
        Collection<Announcement> collection = new ArrayList<>();
        for (Announcement entity: ENTITIES.values()) {
            if (Objects.equals(entity.getTitle(), title)) {
                collection.add(entity);
            }
        }
        return collection;
    }
}