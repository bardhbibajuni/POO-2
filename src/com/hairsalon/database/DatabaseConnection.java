package com.hairsalon.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:salon.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }
}