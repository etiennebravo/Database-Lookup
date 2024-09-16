package com.bravo;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

public class ModifyController {
    @FXML
    private void switchTomainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML private TextField searchField;
    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private Button update;
    @FXML private Button mainMenuButton;
    @FXML private Label updateStatus;

    public void initialize() {
        // Assert file integrity
        assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'modify.fxml'.";
        assert nameField != null : "fx:id=\"nameField\" was not injected: check your FXML file 'modify.fxml'.";
        assert categoryField != null : "fx:id=\"categoryField\" was not injected: check your FXML file 'modify.fxml'.";
        assert priceField != null : "fx:id=\"priceField\" was not injected: check your FXML file 'modify.fxml'.";
        assert stockField != null : "fx:id=\"stockField\" was not injected: check your FXML file 'modify.fxml'.";
        assert update != null : "fx:id=\"update\" was not injected: check your FXML file 'modify.fxml'.";
        assert updateStatus != null : "fx:id=\"updateStatus\" was not injected: check your FXML file 'modify.fxml'.";
        assert mainMenuButton != null : "fx:id=\"mainMenuButton\" was not injected: check your FXML file 'modify.fxml'.";
    }


    /* Based on the fields modified, construct a query and execute the update */
    @FXML
    private void modify() {
        String queryUpdate = "UPDATE products";
        String querySet = "SET ";
        String queryWhere = "WHERE code = ?";

        boolean columnModified[] = {false, false, false, false};

        if (!nameField.getText().trim().isEmpty()) {
            querySet = querySet.concat( "prod_name = ?, ");
            columnModified[0] = true;
        }

        if (!categoryField.getText().trim().isEmpty()) {
            querySet = querySet.concat("category = ?, ");
            columnModified[1] = true;
        }
        
        if (!priceField.getText().trim().isEmpty()) {
            querySet = querySet.concat("price = ?, ");
            columnModified[2] = true;
        }

        if (!stockField.getText().trim().isEmpty()) {
            querySet = querySet.concat("stock = ?, ");
            columnModified[3] = true;
        }

        String result =  queryUpdate + " " + querySet.substring(0, querySet.length() - 2)  + " " + queryWhere;

        execQuery(result, columnModified);
    }

    /**
     * @param sqlQuery String containing query
     * @param column   Determine which columns where chosen by the user
     */
    private void execQuery(String sqlQuery, boolean... column) {
        try (Connection connection = ConnectionPool.getConnection()) {
            try (PreparedStatement sPreparedStatement = connection.prepareStatement(sqlQuery)) {
                int i = 1;  // parameter index
                if (column[0]) {
                    sPreparedStatement.setString(i++, nameField.getText().trim());
                }
                if (column[1]) {
                    sPreparedStatement.setString(i++, categoryField.getText().trim());
                }
                if (column[2]) {
                    sPreparedStatement.setDouble(i++, Double.parseDouble(priceField.getText().trim()));
                }
                if (column[3]) {
                    sPreparedStatement.setInt(i++, Integer.parseInt(stockField.getText().trim()));
                }

                sPreparedStatement.setInt(i, Integer.parseInt(searchField.getText().trim()));
                sPreparedStatement.executeUpdate();

                updateStatusLabel(updateStatus, "green", "UPDATE SUCCESSFUL!");
            };
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();

            updateStatusLabel(updateStatus, "red", "UPDATE FAILED. CHECK YOUR INPUT");
        }
    }

    // TODO: Should probably make a class for utilities

    /** Set a message with a color of choice
     * @param label label fxml object that is going to be manipulated
     * @param color color for text e.g. "red", "green", "orange"
     * @param msg   message that the label will display
     */
    public static void updateStatusLabel(Label label, String color, String msg) {
        String style = "-fx-text-fill: " + color + ";";
        label.setStyle(style);
        label.setText(msg);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> label.setText(""));
        pause.play();
    }
}
