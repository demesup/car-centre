package org.carcrud;

import lombok.extern.slf4j.Slf4j;
import org.carcrud.model.Brand;
import org.carcrud.model.Car;
import org.carcrud.model.CarField;
import org.carcrud.model.FuelType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CarRepository {
    static DBWorker worker = new DBWorker();

    public void create(Car car) {
        var fields = getFields();
        var values = getValues(car);
        String query = "insert ignore into car(" + fields + ") values (" + values + ")";
        executePrepared(query);
    }

    public List<Car> read() {
        String query = "SELECT * FROM car";
        return getCars(query);
    }

    private  Car readCar(ResultSet resultSet) throws SQLException {
        String VIN = resultSet.getString("vin");
        FuelType fuelType = FuelType.valueOf(resultSet.getString("fuel_type").toUpperCase());
        Brand brand = Brand.valueOf(resultSet.getString("brand").toUpperCase());
        String model = resultSet.getString("model");
        int year = resultSet.getInt("year_of");
        int mileage = resultSet.getInt("mileage");
        String stateNumber = resultSet.getString("state_number");
        String additionalDescription = resultSet.getString("additional_description");
        return new Car(VIN, fuelType, brand, model, year, mileage, stateNumber, additionalDescription);
    }

    private  String getFields() {
        return Arrays.stream(CarField.values()).map(f -> f.name().toLowerCase()).collect(Collectors.joining(","));
    }

    private String getValues(Car car) {
        return Arrays.stream(CarField.values()).map(field -> field.getGetter().apply(car).toString()).collect(Collectors.joining("','", "'", "'"));
    }


    public List<Car> search(Map<String, String> map) {
        String query = map.entrySet().stream().map(entry -> entry.getKey() + " = '" + entry.getValue().toLowerCase() + "'").collect(Collectors.joining("and", "select * from car where ", ""));
        return getCars(query);
    }

    private ArrayList<Car> getCars(String query) {
        ArrayList<Car> list = new ArrayList<>();
        try (Statement statement = worker.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(readCar(resultSet));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return list;
    }

    public void delete(Map<String, String> fieldsBy) {
        var query = fieldsBy.entrySet().stream()
                .map(entry -> entry.getKey() + " = '" + entry.getValue() + "'")
                .collect(Collectors.joining(" and ", "delete from car where ", ""));
        executePrepared(query);
    }

    public void update(Map<String, String> where, Map<String, String> update) {
        String updateValues = update.entrySet().stream()
                .map(entry -> entry.getKey() + " = '" + entry.getValue() + "'")
                .collect(Collectors.joining(" , ", "update car set ", ""));

        var whereValues = where.entrySet().stream().map(entry -> entry.getKey() + " = '" + entry.getValue() + "'")
                .collect(Collectors.joining(" and ", " where ", ""));

        String query = updateValues + whereValues;
        executePrepared(query);
    }

    public static void closeConnection() {
        try {
            worker.getConnection().close();
            log.debug("Connection is closed");
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
    }

    private static void executePrepared(String query) {
        try (PreparedStatement statement = worker.getConnection().prepareStatement(query)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
