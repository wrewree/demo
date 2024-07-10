package org.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для управления соединением с базой данных и операциями над ней.
 */
public class WeatherDatabase {

    private static final String URL = "jdbc:mysql://localhost:3306/weatherdb";
    private static final String USER = "root";
    private static final String PASSWORD = "Lenovovibes1228qwerty";

    /**
     * Метод для подключения к базе данных.
     * @return Соединение с базой данных
     * @throws SQLException Если возникла ошибка при подключении
     */
    private Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        checkAndCreateTable(connection);
        return connection;
    }

    /**
     * Метод для проверки наличия таблицы и её создания, если она отсутствует.
     * @param connection Соединение с базой данных
     * @throws SQLException Если возникла ошибка при создании таблицы
     */
    private void checkAndCreateTable(Connection connection) throws SQLException {
        String checkTableQuery = "SELECT COUNT(*) AS count FROM information_schema.tables WHERE table_schema = 'weatherdb' AND table_name = 'weather'";
        String createTableQuery = "CREATE TABLE weather (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "city VARCHAR(100), " +
                "date DATE, " +
                "temp VARCHAR(50), " +
                "feeling VARCHAR(50), " +
                "max_temp VARCHAR(50), " +
                "min_temp VARCHAR(50), " +
                "pressure VARCHAR(50)" +
                ")";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkTableQuery);
             ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next() && rs.getInt("count") == 0) {
                try (PreparedStatement createStmt = connection.prepareStatement(createTableQuery)) {
                    createStmt.executeUpdate();
                }
            }
        }
    }

    /**
     * Метод для сохранения данных о погоде в базу данных.
     * @param city Город
     * @param date Дата
     * @param temp Температура
     * @param feeling Ощущаемая температура
     * @param maxTemp Максимальная температура
     * @param minTemp Минимальная температура
     * @param pressure Давление
     * @throws SQLException Если возникла ошибка при сохранении данных
     */
    public void saveWeatherData(String city, String date, String temp, String feeling, String maxTemp, String minTemp, String pressure) throws SQLException {
        String query = "INSERT INTO weather (city, date, temp, feeling, max_temp, min_temp, pressure) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, city);
            pstmt.setString(2, date);
            pstmt.setString(3, temp);
            pstmt.setString(4, feeling);
            pstmt.setString(5, maxTemp);
            pstmt.setString(6, minTemp);
            pstmt.setString(7, pressure);
            pstmt.executeUpdate();
        }
    }

    /**
     * Метод для получения данных о погоде по дате.
     * @param date Дата
     * @return Список городов и данных о погоде на указанную дату
     * @throws SQLException Если возникла ошибка при получении данных
     */
    public List<String> getWeatherDataByDate(String date) throws SQLException {
        List<String> results = new ArrayList<>();
        String query = "SELECT city, temp, feeling, max_temp, min_temp, pressure FROM weather WHERE date = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String result = String.format("City: %s, Temp: %s, Feeling: %s, Max Temp: %s, Min Temp: %s, Pressure: %s",
                        rs.getString("city"), rs.getString("temp"), rs.getString("feeling"),
                        rs.getString("max_temp"), rs.getString("min_temp"), rs.getString("pressure"));
                results.add(result);
            }
        }
        return results;
    }
}

