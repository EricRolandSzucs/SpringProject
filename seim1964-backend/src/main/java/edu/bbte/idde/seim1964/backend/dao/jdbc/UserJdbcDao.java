package edu.bbte.idde.seim1964.backend.dao.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import edu.bbte.idde.seim1964.backend.config.Config;
import edu.bbte.idde.seim1964.backend.config.ConfigFactory;
import edu.bbte.idde.seim1964.backend.dao.UserDao;
import edu.bbte.idde.seim1964.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserJdbcDao implements UserDao {
    private final HikariDataSource dataSource;
    private static final Logger LOG = LoggerFactory.getLogger(UserJdbcDao.class);
    private int irasi=0;
    private int olvasasi=0;

    private final Boolean logQueries;
    private final Boolean logUpdates;


    public UserJdbcDao() {
        dataSource = DataSourceFactory.getDataSource();
        Config config = ConfigFactory.getConfig();
        logQueries = config.getLogQueries();
        logUpdates = config.getLogUpdates();
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users");

            if(logQueries) {
                LOG.info(String.valueOf(preparedStatement));
            }
            olvasasi++;

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                users.add(createUserObject(resultSet));
            }
        } catch (SQLException exception) {
            LOG.error("Finding all Users failed." + exception);
        }
        return users;
    }

    @Override
    public User create(User entity) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = userInsertBuilder(connection, entity);

            if(logUpdates) {
                LOG.info(String.valueOf(preparedStatement));
            }
            irasi++;

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            LOG.error("User creation failed.", exception);
        }
        return entity;
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = userWhereIdBuilder(connection,
                    "UPDATE Announcements SET UserId = null WHERE UserId = ?", id);

            if(logUpdates) {
                LOG.info(String.valueOf(preparedStatement));
            }
            irasi++;

            preparedStatement.executeUpdate();

            preparedStatement = userWhereIdBuilder(connection,
                    "DELETE FROM Users WHERE UserId = ?", id);

            if(logUpdates) {
                LOG.info(String.valueOf(preparedStatement));
            }
            irasi++;

            preparedStatement.executeUpdate();
            // preparedStatement.executeUpdate();
            LOG.info("User deletion successful.");

        } catch (SQLException exception) {
            LOG.error("User deletion failed." + exception);
        }
    }

    @Override
    public User update(Long id, User entity) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = userUpdateBuilder(connection, entity, id);

            if(logUpdates) {
                LOG.info(String.valueOf(preparedStatement));
            }
            irasi++;

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOG.error("User update failed.", exception);
        }
        return entity;
    }

    @Override
    public User findById(Long id) {
        User user = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = userWhereIdBuilder(connection,
                    "SELECT * FROM Users WHERE UserId = ?", id);

            if(logQueries) {
                LOG.info(String.valueOf(preparedStatement));
            }
            olvasasi++;
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = createUserObject(resultSet);
            }

        } catch (SQLException exception) {
            LOG.error("Finding User by id failed." + exception);
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = userWhereLikeBuilder(connection, username);

            if(logQueries) {
                LOG.info(String.valueOf(preparedStatement));
            }
            olvasasi++;
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = createUserObject(resultSet);
            }

        } catch (SQLException exception) {
            LOG.error("Finding User by username failed." + exception);
        }
        return user;
    }

    @Override
    public Integer findIrasi() {
        return irasi;
    }

    @Override
    public Integer findOlvasasi() {
        return olvasasi;
    }

    private User createUserObject(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getString("Username"),
                resultSet.getString("Name"),
                resultSet.getString("Email"),
                resultSet.getString("Password"),
                resultSet.getString("Phone")
        );
        user.setId(resultSet.getLong("UserId"));
        return user;
    }

    private PreparedStatement userInsertBuilder(Connection connection, User entity) throws SQLException {
        String query = "INSERT INTO Users (Username, Name, Email, Password, Phone) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, entity.getPhone());

        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }

    private PreparedStatement userUpdateBuilder(Connection connection,
                                                User entity, Long id) throws SQLException {
        String query = "UPDATE Users SET Username = ?, Name = ?, Email = ?, Password = ?, Phone = ? "
                + "WHERE UserId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, entity.getPhone());
            preparedStatement.setInt(6, Math.toIntExact(id));
        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }

    private PreparedStatement userWhereIdBuilder(Connection connection,
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

    private PreparedStatement userWhereLikeBuilder(Connection connection,
                                                   String attribute) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT * FROM Users WHERE Username LIKE ?");
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
