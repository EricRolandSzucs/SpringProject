package edu.bbte.idde.seim1964.spring.dao.jpa;

import edu.bbte.idde.seim1964.spring.dao.UserDao;
import edu.bbte.idde.seim1964.spring.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface UserJpaDao extends JpaRepository<User, Long>, UserDao {
}
