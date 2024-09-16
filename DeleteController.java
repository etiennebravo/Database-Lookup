package com.bravo;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DeleteController {
    @FXML
    private void switchTomainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML private TextField searchField;
    @FXML private Label deleteStatus;
    @FXML private Button deleteButton;

    public void initialize() {
        // Assert file integrity
        assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'delete.fxml'.";
        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'delete.fxml'.";
    }

    @FXML
    private void delete() {
        String sqlQuery = "DELETE FROM products WHERE code = ?";

        try (Connection connection = ConnectionPool.getConnection(); 
            PreparedStatement sPreparedStatement = connection.prepareStatement(sqlQuery)) {
            int code = Integer.parseInt(searchField.getText().trim());
            sPreparedStatement.setInt(1, code);

            // show pop up
            FXMLLoader loader = new FXMLLoader(getClass().getResource("popup.fxml"));
            Parent root = loader.load();
            PopController popControl = loader.getController();

            Scene scene = new Scene(root, 512, 300);
            Stage popupStage = new Stage();
            
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(scene);
            popControl.showEntry(code);
            popupStage.showAndWait();

            // Check if the user confirmed the deletion
            if (popControl.isConfirmed()) {
                ModifyController.updateStatusLabel(deleteStatus, "green", "Item deleted successfully!");
                sPreparedStatement.executeUpdate();
            } else {
                ModifyController.updateStatusLabel(deleteStatus, "red", "Item was not deleted.");
                sPreparedStatement.close();
            }
        } catch (IOException ex) {
            System.out.println("Could not load fxml file.");
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input.");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("SQL operation failed.");
            ex.printStackTrace();
        }
    }
}
