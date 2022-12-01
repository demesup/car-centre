package org.carcrud;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;

@Getter
@Setter
public class DBWorker {
    private final String url = "jdbc:mysql://localhost:3306/car_db";
    private static final String user = "root";
    private static final String password = "152120";

    static {
        String sql = "CREATE DATABASE IF NOT EXISTS car_db";
        String sqlTable =
                "CREATE table IF NOT EXISTS car(" +
                "car_id INT primary key AUTO_INCREMENT," +
                "vin VARCHAR(17) unique, " +
                "fuel_type VARCHAR(20), " +
                "brand varchar(50)," +
                "model  varchar(50)," +
                "year_of int," +
                "mileage INT," +
                "state_number varchar(8)," +
                "additional_description varchar(255))";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            Statement statement = conn.createStatement();
            statement.addBatch("use car_db;");
            statement.addBatch(sqlTable);

            stmt.execute();
            statement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection connection;

    {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
