package edu.bbte.idde.seim1964.backend.dao.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import edu.bbte.idde.seim1964.backend.dao.AnnouncementDao;
import edu.bbte.idde.seim1964.backend.model.Announcement;
import edu.bbte.idde.seim1964.backend.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class AnnouncementJdbcDao implements AnnouncementDao {
    private final HikariDataSource dataSource;
    private final CarJdbcDao carjdbcdao = new CarJdbcDao();
    private static final Logger LOG = LoggerFactory.getLogger(AnnouncementJdbcDao.class);

    public AnnouncementJdbcDao() {
        dataSource = DataSourceFactory.getDataSource();
    }

    @Override
    public Collection<Announcement> findAll() {
        Collection<Announcement> announcements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Announcements");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Car currentCar = carjdbcdao.findById(resultSet.getInt("CarId"));
                announcements.add(createAnnouncementObject(resultSet, currentCar));
            }
        } catch (SQLException exception) {
            LOG.error("Finding all Announcements failed." + exception);
        }
        return announcements;
    }

    @Override
    public Announcement create(Announcement entity) {
        try (Connection connection = dataSource.getConnection()) {
            Integer carId = carjdbcdao.create(entity.getCar());
            PreparedStatement preparedStatement = announcementInsertBuilder(connection, entity, carId);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOG.error("Announcement creation failed.", exception);
        }
        return entity;
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = announcementWhereIdBuilder(connection,
                    "SELECT CarId FROM Announcements WHERE AnnouncementId = ?", id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                preparedStatement = announcementWhereIdBuilder(connection,
                        "DELETE FROM Announcements WHERE AnnouncementId = ?", id);

                preparedStatement.executeUpdate();

                carjdbcdao.delete(resultSet.getInt("CarId"));

                LOG.info("Announcement deletion successful.");
            }

        } catch (SQLException exception) {
            LOG.error("Announcement deletion failed." + exception);
        }
    }

    @Override
    public Announcement update(Long id, Announcement entity) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = announcementWhereIdBuilder(connection,
                    "SELECT CarId FROM Announcements WHERE AnnouncementId = ?", id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                carjdbcdao.update(resultSet.getInt("CarId"), entity.getCar());
                preparedStatement = announcementUpdateBuilder(connection, entity, id);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException exception) {
            LOG.error("Announcement update failed.", exception);
        }
        return entity;
    }

    @Override
    public Announcement findById(Long id) {
        Announcement announcement = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    announcementWhereIdBuilder(connection, "SELECT * FROM Announcements WHERE AnnouncementId = ?", id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Car currentCar = carjdbcdao.findById(resultSet.getInt("CarId"));
                announcement = createAnnouncementObject(resultSet, currentCar);
            }

        } catch (SQLException exception) {
            LOG.error("Finding Announcement by id failed." + exception);
        }
        return announcement;
    }

    @Override
    public Collection<Announcement> findByTitle(String title) {
        Collection<Announcement> announcements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = announcementWhereLikeBuilder(connection,
                    title);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Car currentCar = carjdbcdao.findById(resultSet.getInt("CarId"));
                announcements.add(createAnnouncementObject(resultSet, currentCar));
            }
        } catch (SQLException exception) {
            LOG.error("Finding Announcement by title failed." + exception);
        }
        return announcements;
    }

    private Announcement createAnnouncementObject(ResultSet resultSet, Car car) throws SQLException {
        Announcement announcement = new Announcement(
                resultSet.getString("Title"),
                resultSet.getLong("UserId"),
                resultSet.getDate("Date").toLocalDate(),
                car,
                resultSet.getString("Description")
        );
        announcement.setId(resultSet.getLong("AnnouncementId"));
        return announcement;
    }

    private PreparedStatement announcementInsertBuilder(Connection connection,
                                                        Announcement entity, Integer carId) throws SQLException {
        String query = "INSERT INTO Announcements (Title, UserId, Date, CarId, Description) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setInt(2, Math.toIntExact(entity.getUserId()));
            preparedStatement.setDate(3, Date.valueOf(entity.getDate()));
            preparedStatement.setInt(4, carId);
            preparedStatement.setString(5, entity.getDescription());

        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }

    private PreparedStatement announcementUpdateBuilder(Connection connection,
                                                        Announcement entity, Long id) throws SQLException {
        String query = "UPDATE Announcements SET Title = ?, UserId = ?, Date = ?, Description = ? "
                + "WHERE AnnouncementId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setInt(2, Math.toIntExact(entity.getUserId()));
            preparedStatement.setDate(3, Date.valueOf(entity.getDate()));
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setInt(5, Math.toIntExact(id));
        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }

    private PreparedStatement announcementWhereIdBuilder(Connection connection,
                                                         String query, Long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setInt(1, Math.toIntExact(id));
        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }

    private PreparedStatement announcementWhereLikeBuilder(Connection connection,
                                                           String attribute) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT * FROM Announcements WHERE Title LIKE ?");
        try {
            preparedStatement.setString(1, attribute);
        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }
}

