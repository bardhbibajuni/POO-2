package com.hairsalon.controller;

import com.hairsalon.database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {

    @FXML private Label totalAppointmentsLabel;
    @FXML private Label completedLabel;
    @FXML private Label cancelledLabel;
    @FXML private Label aiInsightLabel;

    @FXML private PieChart statusChart;
    @FXML private BarChart<String, Number> serviceChart;

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        int total = getCount("SELECT COUNT(*) FROM appointments");
        int completed = getCount("SELECT COUNT(*) FROM appointments WHERE status = 'Completed'");
        int cancelled = getCount("SELECT COUNT(*) FROM appointments WHERE status = 'Cancelled'");
        int scheduled = getCount("SELECT COUNT(*) FROM appointments WHERE status = 'Scheduled'");

        totalAppointmentsLabel.setText(String.valueOf(total));
        completedLabel.setText(String.valueOf(completed));
        cancelledLabel.setText(String.valueOf(cancelled));

        statusChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Scheduled", scheduled),
                new PieChart.Data("Completed", completed),
                new PieChart.Data("Cancelled", cancelled)
        ));

        loadServiceChart();
        generateAIInsight(total, completed, cancelled, scheduled);
    }

    private int getCount(String sql) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void loadServiceChart() {
        serviceChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Services");

        String sql = """
                SELECT service_name, COUNT(*) AS total
                FROM appointments
                GROUP BY service_name
                """;

        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(
                        rs.getString("service_name"),
                        rs.getInt("total")
                ));
            }

            serviceChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateAIInsight(int total, int completed, int cancelled, int scheduled) {
        if (total == 0) {
            aiInsightLabel.setText("No appointment data available yet. Add appointments to generate insights.");
        } else if (cancelled > completed) {
            aiInsightLabel.setText("AI Insight: Cancellation rate is high. Consider confirming appointments with clients earlier.");
        } else if (scheduled > completed) {
            aiInsightLabel.setText("AI Insight: Many appointments are still scheduled. The salon should prepare staff availability.");
        } else {
            aiInsightLabel.setText("AI Insight: Appointment performance looks stable. Most clients are completing their bookings.");
        }
    }
}


  
