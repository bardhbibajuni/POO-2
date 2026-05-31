package com.hairsalon.controller;

import com.hairsalon.database.DatabaseConnection;
import com.hairsalon.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class AppointmentController {

    @FXML private TextField clientNameField;
    @FXML private ComboBox<String> serviceBox;
    @FXML private ComboBox<String> stylistBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private ComboBox<String> statusBox;
    @FXML private Button submitBtn;
    @FXML private Button deleteBtn;

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Integer> idColumn;
    @FXML private TableColumn<Appointment, String> clientColumn;
    @FXML private TableColumn<Appointment, String> serviceColumn;
    @FXML private TableColumn<Appointment, String> stylistColumn;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private int editingId = -1;

    @FXML
    public void initialize() {
        serviceBox.setItems(FXCollections.observableArrayList(
                "Haircut", "Hair Coloring", "Blow Dry", "Hair Treatment", "Bridal Hairstyle"
        ));
        stylistBox.setItems(FXCollections.observableArrayList(
                "Arta", "Lina", "Dona", "Nora"
        ));
        statusBox.setItems(FXCollections.observableArrayList(
                "Scheduled", "Completed", "Cancelled"
        ));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        stylistColumn.setCellValueFactory(new PropertyValueFactory<>("stylistName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Click row → fill form for editing
        appointmentTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, selected) -> {
                if (selected != null) populateForm(selected);
            }
        );

        loadAppointments();
        setupContextMenu();
    }

    private void populateForm(Appointment a) {
        editingId = a.getId();
        clientNameField.setText(a.getClientName());
        serviceBox.setValue(a.getServiceName());
        stylistBox.setValue(a.getStylistName());
        try { datePicker.setValue(LocalDate.parse(a.getDate())); } catch (Exception ignored) {}
        timeField.setText(a.getTime());
        statusBox.setValue(a.getStatus());

        submitBtn.setText("Update Appointment");
        submitBtn.setStyle("-fx-background-color: #6B3A2A; -fx-text-fill: #F5ECD7; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 10 22; -fx-cursor: hand;");
        if (deleteBtn != null) deleteBtn.setDisable(false);
    }

    @FXML
    public void addAppointment() {
        if (editingId > 0) {
            updateAppointment();
        } else {
            insertAppointment();
        }
    }

    private void insertAppointment() {
        String clientName = clientNameField.getText().trim();
        String service    = serviceBox.getValue();
        String stylist    = stylistBox.getValue();
        String date       = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        String time       = timeField.getText().trim();
        String status     = statusBox.getValue();

        if (clientName.isEmpty() || service == null || stylist == null || date.isEmpty() || time.isEmpty() || status == null) {
            showAlert("Please fill all fields.");
            return;
        }

        String sql = "INSERT INTO appointments (client_name, service_name, stylist_name, date, time, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, clientName);
            stmt.setString(2, service);
            stmt.setString(3, stylist);
            stmt.setString(4, date);
            stmt.setString(5, time);
            stmt.setString(6, status);
            stmt.executeUpdate();
            clearForm();
            loadAppointments();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void updateAppointment() {
        String clientName = clientNameField.getText().trim();
        String service    = serviceBox.getValue();
        String stylist    = stylistBox.getValue();
        String date       = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        String time       = timeField.getText().trim();
        String status     = statusBox.getValue();

        if (clientName.isEmpty() || service == null || stylist == null || date.isEmpty() || time.isEmpty() || status == null) {
            showAlert("Please fill all fields.");
            return;
        }

        String sql = "UPDATE appointments SET client_name=?, service_name=?, stylist_name=?, date=?, time=?, status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, clientName);
            stmt.setString(2, service);
            stmt.setString(3, stylist);
            stmt.setString(4, date);
            stmt.setString(5, time);
            stmt.setString(6, status);
            stmt.setInt(7, editingId);
            stmt.executeUpdate();
            clearForm();
            loadAppointments();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void deleteAppointment() {
        if (editingId <= 0) { showAlert("Select an appointment first."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this appointment?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                String sql = "DELETE FROM appointments WHERE id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, editingId);
                    stmt.executeUpdate();
                    clearForm();
                    loadAppointments();
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    private void loadAppointments() {
        appointments.clear();
        String sql = "SELECT * FROM appointments";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getString("service_name"),
                        rs.getString("stylist_name"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("status")
                ));
            }
            appointmentTable.setItems(appointments);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void setupContextMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        MenuItem delete  = new MenuItem("Delete Selected");
        refresh.setOnAction(e -> loadAppointments());
        delete.setOnAction(e -> deleteAppointment());
        menu.getItems().addAll(refresh, delete);
        appointmentTable.setContextMenu(menu);
    }

    @FXML
    public void clearForm() {
        editingId = -1;
        clientNameField.clear();
        serviceBox.setValue(null);
        stylistBox.setValue(null);
        datePicker.setValue(null);
        timeField.clear();
        statusBox.setValue(null);
        appointmentTable.getSelectionModel().clearSelection();

        submitBtn.setText("Add Appointment");
        submitBtn.setStyle("-fx-background-color: #6B3A2A; -fx-text-fill: #F5ECD7; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 10 22; -fx-cursor: hand;");
        if (deleteBtn != null) deleteBtn.setDisable(true);
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Validation"); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }
}
