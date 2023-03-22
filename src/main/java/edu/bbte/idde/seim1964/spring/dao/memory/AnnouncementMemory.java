package edu.bbte.idde.seim1964.spring.dao.memory;

import edu.bbte.idde.seim1964.spring.dao.AnnouncementDao;
import edu.bbte.idde.seim1964.spring.model.Announcement;
import edu.bbte.idde.seim1964.spring.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("mem")
public class AnnouncementMemory implements AnnouncementDao {

    private static final Map<Long, Announcement> ENTITIES = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final Logger LOG = LoggerFactory.getLogger(AnnouncementMemory.class);

    static {

        Car car = new Car("BMW", "F40", 29630, "Petrol", "White");
        LocalDate date = LocalDate.of(2017, 1, 13);
        Announcement announce1 = new Announcement("Hot BMW", date, car, "Majdnem okay.");
        Announcement announce2 = new Announcement("Cold BMW", date, car, "Okay.");
        Long id1 = ID_GENERATOR.getAndIncrement();
        Long id2 = ID_GENERATOR.getAndIncrement();
        announce1.setId(id1);
        announce2.setId(id2);
        ENTITIES.put(id1, announce1);
        ENTITIES.put(id2, announce2);
    }

    @Override
    public Optional<Announcement> findById(Long id) {
        return Optional.of(ENTITIES.get(id));
    }

    @Override
    public Announcement saveAndFlush(Announcement announcement) {
        if (announcement.getId() == null) {
            Long id = ID_GENERATOR.getAndIncrement();
            announcement.setId(id);
            ENTITIES.put(id, announcement);
            return announcement;
        } else {
            ENTITIES.remove(announcement.getId());
            ENTITIES.put(announcement.getId(), announcement);
        }
        return announcement;
    }

    @Override
    public Collection<Announcement> findAll() {
        return ENTITIES.values();
    }

    @Override
    public void deleteById(Long id) {
        if (ENTITIES.remove(id) == null) {
            LOG.warn("Announcement ID doesn't exist");
        }
    }

    @Override
    public Collection<Announcement> findByTitle(String title) {
        Collection<Announcement> collection = new ArrayList<>();
        for (Announcement entity : ENTITIES.values()) {
            if (Objects.equals(entity.getTitle(), title)) {
                collection.add(entity);
            }
        }
        return collection;
    }

    @Override
    public Long findCarId(Long id) {
        return ENTITIES.get(id).getCar().getId();
    }

}