package com.hairsalon.controller;

import com.hairsalon.database.DatabaseConnection;
import com.hairsalon.model.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicesController {

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField durationField;
    @FXML private Button submitBtn;
    @FXML private Button deleteBtn;

    @FXML private TableView<Service> serviceTable;
    @FXML private TableColumn<Service, Integer> idColumn;
    @FXML private TableColumn<Service, String> nameColumn;
    @FXML private TableColumn<Service, Double> priceColumn;
    @FXML private TableColumn<Service, Integer> durationColumn;

    private final ObservableList<Service> services = FXCollections.observableArrayList();
    private int editingId = -1;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // Click row → fill form
        serviceTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, selected) -> {
                    if (selected != null) populateForm(selected);
                }
        );

        loadServices();
    }

    private void populateForm(Service s) {
        editingId = s.getId();
        nameField.setText(s.getName());
        priceField.setText(String.valueOf(s.getPrice()));
        durationField.setText(String.valueOf(s.getDuration()));

        submitBtn.setText("Update Service");
        submitBtn.setStyle("-fx-background-color: #6B3A2A; -fx-text-fill: #F5ECD7; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 10 22; -fx-cursor: hand;");
        if (deleteBtn != null) deleteBtn.setDisable(false);
    }

    @FXML
    public void addService() {
        if (editingId > 0) {
            updateService();
        } else {
            insertService();
        }
    }

    private void insertService() {
        String name     = nameField.getText().trim();
        String priceStr = priceField.getText().trim();
        String durStr   = durationField.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || durStr.isEmpty()) {
            showAlert("Please fill all fields."); return;
        }

        String sql = "INSERT INTO services (name, price, duration) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, Double.parseDouble(priceStr));
            stmt.setInt(3, Integer.parseInt(durStr));
            stmt.executeUpdate();
            clearForm();
            loadServices();
        } catch (NumberFormatException e) {
            showAlert("Invalid price or duration.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void updateService() {
        String name     = nameField.getText().trim();
        String priceStr = priceField.getText().trim();
        String durStr   = durationField.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || durStr.isEmpty()) {
            showAlert("Please fill all fields."); return;
        }

        String sql = "UPDATE services SET name=?, price=?, duration=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, Double.parseDouble(priceStr));
            stmt.setInt(3, Integer.parseInt(durStr));
            stmt.setInt(4, editingId);
            stmt.executeUpdate();
            clearForm();
            loadServices();
        } catch (NumberFormatException e) {
            showAlert("Invalid price or duration.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void deleteService() {
        if (editingId <= 0) { showAlert("Select a service first."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this service?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                String sql = "DELETE FROM services WHERE id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, editingId);
                    stmt.executeUpdate();
                    clearForm();
                    loadServices();
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    private void loadServices() {
        services.clear();
        String sql = "SELECT * FROM services";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                services.add(new Service(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("duration")
                ));
            }
            serviceTable.setItems(services);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void clearForm() {
        editingId = -1;
        nameField.clear();
        priceField.clear();
        durationField.clear();
        serviceTable.getSelectionModel().clearSelection();

        submitBtn.setText("Add Service");
        submitBtn.setStyle("-fx-background-color: #6B3A2A; -fx-text-fill: #F5ECD7; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 10 22; -fx-cursor: hand;");
        if (deleteBtn != null) deleteBtn.setDisable(true);
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Validation"); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }
}