package com.hairsalon.controller;

import com.hairsalon.database.DatabaseConnection;
import com.hairsalon.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private RadioButton femaleRadio;
    @FXML private RadioButton maleRadio;
    @FXML private CheckBox vipCheckBox;
    @FXML private Button submitBtn;
    @FXML private Button deleteBtn;

    private ToggleGroup genderGroup;

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, Integer> idColumn;
    @FXML private TableColumn<Client, String> nameColumn;
    @FXML private TableColumn<Client, String> phoneColumn;
    @FXML private TableColumn<Client, String> emailColumn;

    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    private int editingId = -1;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        genderGroup = new ToggleGroup();
        femaleRadio.setToggleGroup(genderGroup);
        maleRadio.setToggleGroup(genderGroup);

        // Click row → fill form
        clientTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, selected) -> {
                    if (selected != null) populateForm(selected);
                }
        );

        loadClients();
    }

    private void populateForm(Client c) {
        editingId = c.getId();
        nameField.setText(c.getName());
        phoneField.setText(c.getPhone());
        emailField.setText(c.getEmail() != null ? c.getEmail() : "");

        submitBtn.setText("Update Client");
        submitBtn.setStyle("-fx-background-color: #6B3A2A; -fx-text-fill: #F5ECD7; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 10 22; -fx-cursor: hand;");
        if (deleteBtn != null) deleteBtn.setDisable(false);
    }

    @FXML
    public void addClient() {
        if (editingId > 0) {
            updateClient();
        } else {
            insertClient();
        }
    }
}
