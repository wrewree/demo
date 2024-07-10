package org.example.demo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Модель, которая отвечает за получение данных о погоде и их обновление.
 */
public class WeatherModel {

    private static WeatherModel instance;

    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров извне.
     */
    private WeatherModel() {}

    /**
     * Метод для получения единственного экземпляра класса.
     * @return Единственный экземпляр класса WeatherModel
     */
    public static WeatherModel getInstance() {
        if (instance == null) {
            instance = new WeatherModel();
        }
        return instance;
    }

    /**
     * Метод для обновления данных о погоде на основе указанного города.
     * @param city Город, для которого нужно получить данные о погоде
     * @param controller Контроллер, чтобы обновить представление
     */
    public void updateWeatherData(String city, HelloController controller) {
        String output = getUrlConnect("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=eb06e5e57e84fb896471a8eb0809695b&units=metric");
        if (!output.isEmpty()) {
            JSONObject object = new JSONObject(output);
            String temp = "Temperature " + object.getJSONObject("main").getDouble("temp");
            String feeling = "Feeling " + object.getJSONObject("main").getDouble("feels_like");
            String maxTemp = "Max " + object.getJSONObject("main").getDouble("temp_max");
            String minTemp = "Min " + object.getJSONObject("main").getDouble("temp_min");
            String pressure = "Pressure " + object.getJSONObject("main").getDouble("pressure");
            // Обновление представления
            controller.updateView(temp, feeling, maxTemp, minTemp, pressure);
        }
    }

    /**
     * Метод для получения данных по указанному URL-адресу.
     * @param urlAddress URL-адрес для запроса данных
     * @return Строка с содержимым данных, полученных по URL-адресу
     */
    private static String getUrlConnect(String urlAddress) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.toString();
    }
}


