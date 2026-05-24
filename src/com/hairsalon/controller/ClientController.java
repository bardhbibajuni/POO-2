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
}
