package edu.bbte.idde.seim1964.spring.dao.jdbc;

import edu.bbte.idde.seim1964.spring.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
@Profile("jdbc")
public class CarJdbcDao {

    @Autowired
    private DataSource dataSource;
    private static final Logger LOG = LoggerFactory.getLogger(CarJdbcDao.class);

    public Integer create(Car car) {
        Integer carId = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = carInsertBuilder(connection, car);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                carId = generatedKeys.getInt(1);
            }
        } catch (SQLException exception) {
            LOG.error("Car creation failed.", exception);
        }

        return carId;
    }

    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = carWhereIdBuilder(connection, "DELETE FROM Cars WHERE id = ?", id);
            preparedStatement.executeUpdate();
            LOG.info("Car deletion successful.");
        } catch (SQLException exception) {
            LOG.error("Car deletion failed." + exception);
        }
    }

    public void update(Integer id, Car car) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = announcementUpdateBuilder(connection, car, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOG.error("Announcement update failed.", exception);
        }
    }

    public Car findById(Integer id) {
        Car car = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = carWhereIdBuilder(connection,
                    "SELECT * FROM Cars WHERE id = ?", id);
            ResultSet resultSetCar = preparedStatement.executeQuery();
            if (resultSetCar.next()) {
                car = createCarObject(resultSetCar);
            }

        } catch (SQLException exception) {
            LOG.error("Finding Announcement by id failed." + exception);
        }
        return car;
    }

    private Car createCarObject(ResultSet resultSet) throws SQLException {
        Car car = new Car(
                resultSet.getString("Brand"),
                resultSet.getString("Model"),
                resultSet.getInt("Price"),
                resultSet.getString("Type"),
                resultSet.getString("Color")
        );
        car.setId(resultSet.getLong("id"));
        return car;
    }

    private PreparedStatement carInsertBuilder(Connection connection, Car car) throws SQLException {
        String query = "INSERT INTO Cars (Brand, Model, Price, Type, Color) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        try {
            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setString(2, car.getModel());
            preparedStatement.setInt(3, car.getPrice());
            preparedStatement.setString(4, car.getType());
            preparedStatement.setString(5, car.getColor());

        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }

    private PreparedStatement announcementUpdateBuilder(Connection connection,
                                                        Car car, Integer id) throws SQLException {
        String query = "UPDATE Cars SET Brand = ?, Model = ?, Price = ?, Type = ?, Color = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setString(2, car.getModel());
            preparedStatement.setInt(3, car.getPrice());
            preparedStatement.setString(4, car.getType());
            preparedStatement.setString(5, car.getColor());
            preparedStatement.setInt(6, id);
        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }

    private PreparedStatement carWhereIdBuilder(Connection connection,
                                                String query, Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try {
            preparedStatement.setInt(1, id);
        } catch (SQLException exception) {
            try (preparedStatement) {
                throw exception;
            }
        }
        return preparedStatement;
    }
}
