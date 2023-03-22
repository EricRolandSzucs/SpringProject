package edu.bbte.idde.seim1964.backend.dao.memory;

import edu.bbte.idde.seim1964.backend.dao.UserDao;

import edu.bbte.idde.seim1964.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserMemory implements UserDao {

    private static final Map<Long, User> ENTITIES = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final Logger LOG = LoggerFactory.getLogger(UserMemory.class);

    static {

        User user1 = new User("bruh", "Bruh", "bruh@gmail.com", "bruh", "0722745045");
        User user2 = new User("broh", "Broh", "broh@gmail.com", "broh", "0722755045");
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
    public User create(User entity) {
        Long id = ID_GENERATOR.getAndIncrement();
        entity.setId(id);
        return ENTITIES.put(id, entity);
    }

    @Override
    public void delete(Long id) {
        if (ENTITIES.remove(id) == null) {
            LOG.warn("User ID doesn't exist");
        }
    }

    @Override
    public User update(Long id, User entity) {
        if (ENTITIES.remove(id) == null) {
            LOG.warn("User ID doesn't exist");
        }
        entity.setId(id);
        return ENTITIES.put(id, entity);
    }

    @Override
    public User findById(Long id) {
        return ENTITIES.get(id);
    }

    @Override
    public User findByUsername(String username) {
        User use = null;
        for (User entity: ENTITIES.values()) {
            if (Objects.equals(entity.getUsername(), username)) {
                use = entity;
                break;
            }
        }
        return use;
    }

    @Override
    public Integer findIrasi() {
        return null;
    }

    @Override
    public Integer findOlvasasi() {
        return null;
    }
}
