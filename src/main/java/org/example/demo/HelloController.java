package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер, который управляет представлением и обрабатывает действия пользователя.
 */
public class HelloController {

    @FXML
    private TextField city;

    @FXML
    private Text feeling;

    @FXML
    private Button getData;

    @FXML
    private Text maxTemp;

    @FXML
    private Text minTemp;

    @FXML
    private Text pressure;

    @FXML
    private Text temp;

    @FXML
    private TextField date;

    @FXML
    private Button getDataByDate;

    private WeatherModel model;
    private WeatherDatabase database;

    /**
     * Метод инициализации модели.
     * @param model Модель погоды
     */
    public void initModel(WeatherModel model) {
        this.model = model;
        this.database = new WeatherDatabase();
    }

    /**
     * Метод инициализации, который устанавливает обработчики для кнопок.
     */
    @FXML
    void initialize() {
        getData.setOnAction(actionEvent -> {
            String getUserCity = city.getText().trim();
            model.updateWeatherData(getUserCity, this);
        });

        getDataByDate.setOnAction(actionEvent -> {
            String dateInput = date.getText().trim();
            if (!dateInput.isEmpty()) {
                try {
                    List<String> weatherDataList = database.getWeatherDataByDate(dateInput);
                    weatherDataList.forEach(System.out::println); // Выводим данные в консоль
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Метод для обновления данных о погоде в представлении и сохранения их в базу данных.
     * @param temp Температура
     * @param feeling Ощущаемая температура
     * @param maxTemp Максимальная температура
     * @param minTemp Минимальная температура
     * @param pressure Давление
     */
    public void updateView(String temp, String feeling, String maxTemp, String minTemp, String pressure) {
        this.temp.setText(temp);
        this.feeling.setText(feeling);
        this.maxTemp.setText(maxTemp);
        this.minTemp.setText(minTemp);
        this.pressure.setText(pressure);

        // Сохранение данных в базу данных
        String cityName = city.getText().trim();
        String currentDate = LocalDate.now().toString();
        try {
            database.saveWeatherData(cityName, currentDate, temp, feeling, maxTemp, minTemp, pressure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


