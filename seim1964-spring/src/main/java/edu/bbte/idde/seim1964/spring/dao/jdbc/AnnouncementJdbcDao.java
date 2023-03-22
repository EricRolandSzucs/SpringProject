package edu.bbte.idde.seim1964.spring.dao.jdbc;

import edu.bbte.idde.seim1964.spring.dao.AnnouncementDao;
import edu.bbte.idde.seim1964.spring.model.Announcement;
import edu.bbte.idde.seim1964.spring.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class AnnouncementJdbcDao implements AnnouncementDao {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CarJdbcDao carjdbcdao;

    private static final Logger LOG = LoggerFactory.getLogger(AnnouncementJdbcDao.class);

    @Override
    public Collection<Announcement> findAll() {
        Collection<Announcement> announcements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Announcements");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Car currentCar = carjdbcdao.findById(resultSet.getInt("car_id"));
                announcements.add(createAnnouncementObject(resultSet, currentCar));
            }
        } catch (SQLException exception) {
            LOG.error("Finding all Announcements failed." + exception);
        }
        return announcements;
    }

    @Override
    public Announcement saveAndFlush(Announcement entity) {
        LOG.info(String.valueOf(entity.getId()));
        try (Connection connection = dataSource.getConnection()) {
            if (entity.getId() == null) {
                Integer carId = carjdbcdao.create(entity.getCar());
                entity.getCar().setId(Long.valueOf(carId));
                PreparedStatement preparedStatement = announcementInsertBuilder(connection, entity, carId);
                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            } else {
                PreparedStatement preparedStatement = announcementWhereIdBuilder(connection,
                        "SELECT car_id FROM Announcements WHERE Id = ?", entity.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int carId = resultSet.getInt("car_id");
                    entity.getCar().setId((long) carId);
                    carjdbcdao.update(carId, entity.getCar());
                    preparedStatement = announcementUpdateBuilder(connection, entity, entity.getId());
                    preparedStatement.executeUpdate();

                } else {
                    return null;
                }

            }
        } catch (SQLException exception) {
            LOG.error("Announcement creation failed.", exception);
        }
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = announcementWhereIdBuilder(connection,
                    "SELECT car_id FROM Announcements WHERE Id = ?", id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                preparedStatement = announcementWhereIdBuilder(connection,
                        "DELETE FROM Announcements WHERE Id = ?", id);

                preparedStatement.executeUpdate();

                carjdbcdao.delete(resultSet.getInt("car_id"));

                LOG.info("Announcement deletion successful.");
            }

        } catch (SQLException exception) {
            LOG.error("Announcement deletion failed." + exception);
        }
    }

    @Override
    public Optional<Announcement> findById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    announcementWhereIdBuilder(connection, "SELECT * FROM Announcements WHERE Id = ?", id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Car currentCar = carjdbcdao.findById(resultSet.getInt("car_id"));
                Announcement announcement = createAnnouncementObject(resultSet, currentCar);
                return Optional.of(announcement);
            }

        } catch (SQLException exception) {
            LOG.error("Finding Announcement by id failed." + exception);
        }
        return Optional.empty();

    }

    @Override
    public Collection<Announcement> findByTitle(String title) {
        Collection<Announcement> announcements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = announcementWhereLikeBuilder(connection,
                    title);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Car currentCar = carjdbcdao.findById(resultSet.getInt("car_id"));
                announcements.add(createAnnouncementObject(resultSet, currentCar));
            }
        } catch (SQLException exception) {
            LOG.error("Finding Announcement by title failed." + exception);
        }
        return announcements;
    }

    @Override
    public Long findCarId(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = announcementWhereIdBuilder(connection,
                    "SELECT car_id FROM announcements Where id = ?", id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.getInt("car_id");

        } catch (SQLException exception) {
            LOG.error("Finding Announcement by title failed." + exception);
        }
        return -1L;
    }

    private Announcement createAnnouncementObject(ResultSet resultSet, Car car) throws SQLException {
        Announcement announcement = new Announcement(
                resultSet.getString("Title"),
                resultSet.getDate("Date").toLocalDate(),
                car,
                resultSet.getString("Description")
        );
        announcement.setId(resultSet.getLong("Id"));
        return announcement;
    }

    private PreparedStatement announcementInsertBuilder(Connection connection,
                                                        Announcement entity, Integer carId) throws SQLException {
        String query = "INSERT INTO Announcements (Title, Date, car_id, Description) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        try {
            preparedStatement.setString(1, entity.getTitle());
            // preparedStatement.setInt(2, Math.toIntExact(entity.getUserId()));
            preparedStatement.setDate(2, Date.valueOf(entity.getDate()));
            preparedStatement.setInt(3, carId);
            preparedStatement.setString(4, entity.getDescription());

        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }

    private PreparedStatement announcementUpdateBuilder(Connection connection,
                                                        Announcement entity, Long id) throws SQLException {
        String query = "UPDATE Announcements SET Title = ?, Date = ?, Description = ? "
                + "WHERE Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        try {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setDate(2, Date.valueOf(entity.getDate()));
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setInt(4, Math.toIntExact(id));
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

