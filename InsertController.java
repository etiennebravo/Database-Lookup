package com.bravo;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertController {
    @FXML
    private void switchTomainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private Button insert;
    @FXML private Label insertStatus;
    
    public void initialize() {
        // Assert file integrity
        assert nameField != null : "fx:id=\"nameField\" was not injected: check your FXML file 'insert.fxml'.";
        assert categoryField != null : "fx:id=\"categoryField\" was not injected: check your FXML file 'insert.fxml'.";
        assert priceField != null : "fx:id=\"priceField\" was not injected: check your FXML file 'insert.fxml'.";
        assert stockField != null : "fx:id=\"stockField\" was not injected: check your FXML file 'insert.fxml'.";
        assert insert != null : "fx:id=\"insert\" was not injected: check your FXML file 'insert.fxml'.";
        assert insertStatus != null : "fx:id=\"insertStatus\" was not injected: check your FXML file 'insert.fxml'.";
    }

    @FXML
    private void insert() {
        String sqlQuery = "INSERT INTO products (prod_name, category, price, stock) Values (?, ?, ?, ?);";

        // All fields must be used
        if (nameField.getText().trim().isEmpty() ||
            categoryField.getText().trim().isEmpty() ||
            priceField.getText().trim().isEmpty() ||
            stockField.getText().trim().isEmpty()) {
            ModifyController.updateStatusLabel(insertStatus, "orange", "Please fill all fields");
            return;
        }

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement sPreparedStatement = connection.prepareStatement(sqlQuery)) {

                sPreparedStatement.setString(1, nameField.getText().trim());
                sPreparedStatement.setString(2, categoryField.getText().trim());
                sPreparedStatement.setDouble(3, Double.parseDouble(priceField.getText().trim()));
                sPreparedStatement.setInt(4, Integer.parseInt(stockField.getText().trim()));

                sPreparedStatement.executeUpdate();
                ModifyController.updateStatusLabel(insertStatus, "green", "Item added successfully!");
            } catch (SQLException | NumberFormatException ex) {
                ModifyController.updateStatusLabel(insertStatus, "red", "Insertion Failed, i dont know why buh, figurit out buh.");
                ex.printStackTrace();
            }
    }
}
