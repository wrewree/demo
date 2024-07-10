package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Главный класс приложения, который запускает JavaFX приложение и инициализирует модель, представление и контроллер.
 */
public class HelloApplication extends Application {
    /**
     * Метод запуска приложения, который инициализирует модель, загружает представление из FXML и настраивает контроллер.
     * @param primaryStage Главное окно приложения
     * @throws Exception Исключение, если что-то пошло не так при инициализации
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Создание модели
        WeatherModel model = WeatherModel.getInstance();

        // Загрузка представления
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 509, 607);

        // Создание и настройка контроллера
        HelloController controller = fxmlLoader.getController();
        controller.initModel(model);

        // Установка сцены и отображение
        primaryStage.setTitle("Weather");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Метод main, который запускает приложение.
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }
}

