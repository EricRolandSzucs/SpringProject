package edu.bbte.idde.seim1964.spring.dao.memory;

import edu.bbte.idde.seim1964.spring.dao.UserDao;
import edu.bbte.idde.seim1964.spring.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("mem")
public class UserMemory implements UserDao {

    private static final Map<Long, User> ENTITIES = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final Logger LOG = LoggerFactory.getLogger(UserMemory.class);

    static {

        User user1 = new User("bruh", "Bruh", "bruh@gmail.com", "bruh", "0722745045", null);
        User user2 = new User("broh", "Broh", "broh@gmail.com", "broh", "0722755045", null);
        Long id1 = ID_GENERATOR.getAndIncrement();
        Long id2 = ID_GENERATOR.getAndIncrement();
        user1.setId(id1);
        user2.setId(id2);
        ENTITIES.put(id1, user1);
        ENTITIES.put(id2, user2);
    }

    @Override
    public Collection<User> findAll() {
        return ENTITIES.values();
    }

    @Override
    public User saveAndFlush(User entity) {
        if (entity.getId() == null) {
            Long id = ID_GENERATOR.getAndIncrement();
            entity.setId(id);
            return ENTITIES.put(id, entity);
        } else {
            ENTITIES.remove(entity.getId());
            return ENTITIES.put(entity.getId(), entity);
        }
    }

    @Override
    public void deleteById(Long id) {
        if (ENTITIES.remove(id) == null) {
            LOG.warn("User ID doesn't exist");
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.of(ENTITIES.get(id));
    }

}