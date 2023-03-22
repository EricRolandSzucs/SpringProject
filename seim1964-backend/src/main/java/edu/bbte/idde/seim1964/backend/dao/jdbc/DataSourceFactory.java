package edu.bbte.idde.seim1964.backend.dao.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.bbte.idde.seim1964.backend.config.Config;
import edu.bbte.idde.seim1964.backend.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceFactory {

    private static HikariDataSource dataSource;
    private static final Logger LOG = LoggerFactory.getLogger(AnnouncementJdbcDao.class);

    public static synchronized HikariDataSource getDataSource() {
        if (dataSource == null) {
            Config config = ConfigFactory.getConfig();
            HikariConfig hikariConfig = new HikariConfig();

            hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getJdbcUrl() + "/"
                    + config.getJdbcDatabase() + "?useSSL=false");
            LOG.info("jdbc:mysql://" + config.getJdbcUrl() + "/" + config.getJdbcDatabase() + "?useSSL=false");
            hikariConfig.setUsername(config.getJdbcUser());
            hikariConfig.setPassword(config.getJdbcPassword());
            hikariConfig.setDriverClassName(config.getJdbcDriver());
            hikariConfig.setMaximumPoolSize(config.getJdbcPoolSize());

            dataSource = new HikariDataSource(hikariConfig);
        }
        return dataSource;
    }
}
