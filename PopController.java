package com.bravo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PopController {
    @FXML private Button confirmButton;
    @FXML private Button denyButton;
    @FXML private TableView<product> tableView;
    @FXML private TableColumn<product, String> codeColumn;
    @FXML private TableColumn<product, String> nameColumn;
    @FXML private TableColumn<product, String> categoryColumn;
    @FXML private TableColumn<product, String> priceColumn;
    @FXML private TableColumn<product, String> stockColumn;


    private ObservableList<product> data = FXCollections.observableArrayList();;
    private boolean deletionConfirmed = false;

    @FXML
    private void initialize() {
        // assert file integrity
        assert confirmButton != null : "fx:id=\"confirmButton\" was not injected: check your FXML file 'popup.fxml' .";
        assert denyButton != null : "fx:id=\"denyButton\" was not injected: check your FXML file 'popup.fxml' .";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'popup.fxml' .";
        assert codeColumn != null : "fx:id=\"codeColumn\" was not injected: check your FXML file 'popup.fxml' .";
        assert nameColumn != null : "fx:id=\"nameColumn\" was not injected: check your FXML file 'popup.fxml' .";
        assert categoryColumn != null : "fx:id=\"categoryColumn\" was not injected: check your FXML file 'popup.fxml' .";
        assert priceColumn != null : "fx:id=\"priceColumn\" was not injected: check your FXML file 'popup.fxml' .";
        assert stockColumn != null : "fx:id=\"stockColumn\" was not injected: check your FXML file 'popup.fxml' .";

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        tableView.setItems(data);

        confirmButton.setOnAction(event -> confirmDeletion());
        denyButton.setOnAction(event -> denyDeletion());
    }

    public void showEntry(int code) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            String query = "SELECT * FROM products WHERE code = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(new product(resultSet.getInt("code"), 
                                    resultSet.getString("prod_name"), 
                                    resultSet.getString("category"),
                                    resultSet.getDouble("price"),
                                    resultSet.getInt("stock")));
            }
        }
    }

    public void denyDeletion() {
        deletionConfirmed = false;
        closePopup();
    }

    public void confirmDeletion() {
        deletionConfirmed = true;
        closePopup();
    }

    public boolean isConfirmed() {
        return deletionConfirmed;
    }

    public void closePopup() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}
