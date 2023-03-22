package edu.bbte.idde.seim1964.spring.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
@Profile("jdbc")
public class TablesDao {

    @Autowired
    private DataSource dataSource;

    private static final Logger LOG = LoggerFactory.getLogger(TablesDao.class);

    @PostConstruct
    public void createTables() {

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Cars (\n"
                    + "   id INT NOT NULL AUTO_INCREMENT,\n"
                    + "   brand VARCHAR(255) NOT NULL,\n"
                    + "   color VARCHAR(255) NOT NULL,\n"
                    + "   model VARCHAR(255) NOT NULL,\n"
                    + "   price FLOAT NOT NULL,\n"
                    + "   type VARCHAR(255) NOT NULL,\n"
                    + "   CONSTRAINT PK_Cars PRIMARY KEY (id)\n"
                    + ")");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (\n"
                    + "    id INT NOT NULL AUTO_INCREMENT,\n"
                    + "    email VARCHAR(255) NOT NULL,\n"
                    + "    name VARCHAR(255) NOT NULL,\n"
                    + "    password VARCHAR(255) NOT NULL,\n"
                    + "    phone VARCHAR(20) NOT NULL,\n"
                    + "    username VARCHAR(255) UNIQUE NOT NULL,\n"
                    + "    CONSTRAINT PK_Users PRIMARY KEY (id)\n"
                    + ")");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS announcements (\n"
                    + "    id INT NOT NULL AUTO_INCREMENT,\n"
                    + "    date DATE NOT NULL,\n"
                    + "    description VARCHAR(255),\n"
                    + "    title VARCHAR(255) NOT NULL,\n"
                    + "    car_id INT NOT NULL,\n"
                    + "    CONSTRAINT PK_Announcement PRIMARY KEY (id),\n"
                    + "    CONSTRAINT FK_CarId FOREIGN KEY (car_id) REFERENCES cars(id)\n"
                    + ")");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users_announcements (\n"
                    + "    user_id INT NOT NULL,\n"
                    + "    announcements_id INT NOT NULL,\n"
                    + "    CONSTRAINT PK_User_Announcement PRIMARY KEY (user_id, announcements_id),\n"
                    + "    CONSTRAINT FK_UserId FOREIGN KEY (user_id) REFERENCES users(id), \n"
                    + "    CONSTRAINT FK_AnnouncementId FOREIGN KEY (announcements_id) REFERENCES announcements(id)\n"
                    + ")");

            LOG.info("Table creation successful.");
        } catch (SQLException exception) {
            LOG.error("Table creation failed.", exception);
        }

    }
}
