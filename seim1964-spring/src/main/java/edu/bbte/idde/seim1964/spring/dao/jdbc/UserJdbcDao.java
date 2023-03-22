package edu.bbte.idde.seim1964.spring.dao.jdbc;

import edu.bbte.idde.seim1964.spring.dao.UserDao;
import edu.bbte.idde.seim1964.spring.model.Announcement;
import edu.bbte.idde.seim1964.spring.model.User;
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
public class UserJdbcDao implements UserDao {

    @Autowired
    private DataSource dataSource;

    private static final Logger LOG = LoggerFactory.getLogger(UserJdbcDao.class);

    @Override
    public Collection<User> findAll() {
        Collection<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users");
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
    public User saveAndFlush(User entity) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement;
            if (entity.getId() == null) {
                preparedStatement = userInsertBuilder(connection, entity);
            } else {
                preparedStatement = userUpdateBuilder(connection, entity, entity.getId());
            }
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOG.error("User creation/update failed.", exception);
        }
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = userWhereIdBuilder(connection,
                    "DELETE FROM Users WHERE UserId = ?", id);

            preparedStatement.executeUpdate();

            preparedStatement.executeUpdate();
            LOG.info("User deletion successful.");

        } catch (SQLException exception) {
            LOG.error("User deletion failed." + exception);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = userWhereIdBuilder(connection,
                    "SELECT * FROM Users WHERE id = ?", id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = createUserObject(resultSet);
                return Optional.of(user);
            }

        } catch (SQLException exception) {
            LOG.error("Finding User by id failed." + exception);
        }
        return Optional.empty();
    }

    private User createUserObject(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getString("Username"),
                resultSet.getString("Name"),
                resultSet.getString("Email"),
                resultSet.getString("Password"),
                resultSet.getString("Phone"),
                new ArrayList<Announcement>()
        );
        user.setId(resultSet.getLong("id"));
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
                + "WHERE id = ?";
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
}
