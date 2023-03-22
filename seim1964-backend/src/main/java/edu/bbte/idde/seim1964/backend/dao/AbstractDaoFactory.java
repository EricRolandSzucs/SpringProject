package edu.bbte.idde.seim1964.backend.dao;

import edu.bbte.idde.seim1964.backend.config.ConfigFactory;
import edu.bbte.idde.seim1964.backend.dao.jdbc.JdbcDaoFactory;
import edu.bbte.idde.seim1964.backend.dao.memory.MemoryDaoFactory;

public abstract class AbstractDaoFactory {
    private static DaoFactory daoFactory;

    public static synchronized DaoFactory getDaoFactory() {
        if (daoFactory == null) {
            String type = ConfigFactory.getConfig().getDaoType();
            if ("jdbc".equalsIgnoreCase(type)) {
                daoFactory = new JdbcDaoFactory();
            } else {
                daoFactory = new MemoryDaoFactory();
            }
        }

        return daoFactory;
    }
}
