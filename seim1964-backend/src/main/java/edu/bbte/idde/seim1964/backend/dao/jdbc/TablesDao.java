package edu.bbte.idde.seim1964.backend.dao.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TablesDao {
    private final HikariDataSource dataSource;
    private static final Logger LOG = LoggerFactory.getLogger(TablesDao.class);

    public TablesDao() {
        dataSource = DataSourceFactory.getDataSource();
        createTables();
    }

    private void createTables() {

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Cars (\n"
                    + "   CarId INT NOT NULL AUTO_INCREMENT,\n"
                    + "   Brand VARCHAR(255) NOT NULL,\n"
                    + "   Model VARCHAR(255) NOT NULL,\n"
                    + "   Price FLOAT NOT NULL,\n"
                    + "   Type VARCHAR(255) NOT NULL,\n"
                    + "   Color VARCHAR(255) NOT NULL,\n"
                    + "   CONSTRAINT PK_Cars PRIMARY KEY (CarId)\n"
                    + ")");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Users (\n"
                    + "    UserId INT NOT NULL AUTO_INCREMENT,\n"
                    + "    Username VARCHAR(255) UNIQUE NOT NULL,\n"
                    + "    Name VARCHAR(255) NOT NULL,\n"
                    + "    Email VARCHAR(255) NOT NULL,\n"
                    + "    Password VARCHAR(255) NOT NULL,\n"
                    + "    Phone VARCHAR(20) NOT NULL,\n"
                    + "    CONSTRAINT PK_Users PRIMARY KEY (UserId)\n"
                    + ")");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Announcements (\n"
                    + "    AnnouncementId INT NOT NULL AUTO_INCREMENT,\n"
                    + "    Title VARCHAR(255) NOT NULL,\n"
                    + "    UserId INT,\n"
                    + "    Date DATE NOT NULL,\n"
                    + "    CarId INT NOT NULL,\n"
                    + "    Description VARCHAR(255),\n"
                    + "    CONSTRAINT PK_Announcement PRIMARY KEY (AnnouncementId),\n"
                    + "    CONSTRAINT FK_CarId FOREIGN KEY (CarId) REFERENCES Cars(CarId),\n"
                    + "    CONSTRAINT FK_UserId FOREIGN KEY (UserId) REFERENCES Users(UserId)\n"
                    + ")");

            LOG.info("Table creation successful.");
        } catch (SQLException exception) {
            LOG.error("Table creation failed.", exception);
        }

    }
}
