package edu.bbte.idde.seim1964.backend.dao;

import edu.bbte.idde.seim1964.backend.model.User;

public interface UserDao extends Dao<User> {
    User findByUsername(String username);
    Integer findIrasi();
    Integer findOlvasasi();
}

