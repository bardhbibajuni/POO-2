package com.hairsalon.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private VBox contentArea;

    @FXML
    public void showAppointments() {
        loadPage("/resources/view/appointments.fxml");
    }

    @FXML
    public void showClients() {
        loadPage("/resources/view/clients.fxml");
    }

    @FXML
    public void showDashboard() {
        loadPage("/resources/view/dashboard.fxml");
    }

    @FXML
    public void showHelp() {
        loadPage("/resources/view/help.fxml");
    }
    @FXML
    public void showServices() {
        loadPage("/resources/view/services.fxml");
    }
    @FXML
    public void setAlbanian() {
        com.hairsalon.util.LanguageManager.setLanguage("sq");
        System.out.println("Language changed to Albanian");
    }

    @FXML
    public void setEnglish() {
        com.hairsalon.util.LanguageManager.setLanguage("en");
        System.out.println("Language changed to English");
    }

    private void loadPage(String fxmlPath) {
        try {
            Node page = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}