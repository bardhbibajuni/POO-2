package com.hairsalon;

import com.hairsalon.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import com.hairsalon.controller.MainController;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitializer.initializeDatabase();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/resources/view/main.fxml")
        );

        Scene scene = new Scene(loader.load(), 1000, 650);

        scene.getStylesheets().add(
                getClass().getResource("/resources/css/style.css").toExternalForm()
        );

        MainController controller = loader.getController();

        scene.getAccelerators().put(
                KeyCombination.keyCombination("Ctrl+A"),
                controller::showAppointments
        );

        scene.getAccelerators().put(
                KeyCombination.keyCombination("Ctrl+C"),
                controller::showClients
        );

        scene.getAccelerators().put(
                KeyCombination.keyCombination("Ctrl+D"),
                controller::showDashboard
        );

        scene.getAccelerators().put(
                KeyCombination.keyCombination("Ctrl+H"),
                controller::showHelp
        );

        stage.setTitle("Hair Salon Scheduler");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}