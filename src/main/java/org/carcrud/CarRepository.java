package org.carcrud;

import lombok.extern.slf4j.Slf4j;
import org.carcrud.model.Brand;
import org.carcrud.model.Car;
import org.carcrud.model.FuelType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CarRepository {
    static DBWorker worker = new DBWorker();

    public void createCar(Car car) {
        var fields = getFields();
        var values = getValues(car);
        String query = "insert ignore into car(" + fields + ") values (" + values + ")";
        try (PreparedStatement statement = worker.getConnection().prepareStatement(query)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFields() {

        return CarController.CarField.VIN.name().toLowerCase() + "," +
                CarController.CarField.FUEL_TYPE.name().toLowerCase() + "," +
                CarController.CarField.BRAND.name().toLowerCase() + "," +
                CarController.CarField.MODEL.name().toLowerCase() + "," +
                "year_of" + "," +
                CarController.CarField.MILEAGE.name().toLowerCase() + "," +
                CarController.CarField.STATE_NUMBER.name().toLowerCase() + "," +
                CarController.CarField.ADDITIONAL_DESCRIPTION.name().toLowerCase();

    }

    private String getValues(Car car) {
        return "'" + car.getVIN() + "'" +
                "," +
                "'" + car.getFuelType() + "'" +
                "," +
                "'" + car.getBrand() + "'" +
                "," +
                "'" + car.getModel() + "'" +
                "," +
                "'" + car.getYearOf() + "'" +
                "," +
                "'" + car.getMileage() + "'" +
                "," +
                "'" + car.getStateNumber() + "'" +
                "," +
                "'" + car.getAdditionalDescription() + "'";
    }

    public List<Car> read() {
        List<Car> list = new ArrayList<>();

        String query = "SELECT * FROM car";
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

    private static Car readCar(ResultSet resultSet) throws SQLException {
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


    public static void closeConnection() {
        try {
            worker.getConnection().close();
            log.debug("Connection is closed");
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
    }
}
