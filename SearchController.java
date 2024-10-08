package com.bravo;

import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;

public class SearchController {

    @FXML
    private void switchTomainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML private TextField searchField;
    @FXML private TableView<product> tableView;
    @FXML private TableColumn<product, String> codeColumn;
    @FXML private TableColumn<product, String> nameColumn;
    @FXML private TableColumn<product, String> categoryColumn;
    @FXML private TableColumn<product, String> priceColumn;
    @FXML private TableColumn<product, String> stockColumn;
    @FXML private ComboBox<String> productCombo;

    private ObservableList<product> data = FXCollections.observableArrayList();

    public void initialize() {
        // Assert file integrity
        assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'search.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'search.fxml'.";
        assert codeColumn != null : "fx:id=\"codeColumn\" was not injected: check your FXML file 'search.fxml'.";
        assert nameColumn != null : "fx:id=\"nameColumn\" was not injected: check your FXML file 'search.fxml'.";
        assert categoryColumn != null : "fx:id=\"categoryColumn\" was not injected: check your FXML file 'search.fxml'.";
        assert priceColumn != null : "fx:id=\"priceColumn\" was not injected: check your FXML file 'search.fxml'.";
        assert stockColumn != null : "fx:id=\"stockColumn\" was not injected: check your FXML file 'search.fxml'.";
        assert productCombo != null : "fx:id=\"productCombo\" was not injected: check your FXML file 'search.fxml'.";

        // initialize 
        productCombo.setItems(FXCollections.observableArrayList("show all", "code", "prod_name", "category", "price", "stock"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        tableView.setItems(data);
    }

    @FXML
    private void search() throws SQLException {
        // Input from textfield
        String searchText = searchField.getText().toLowerCase();
        data.clear();
    
        // Category from dropdown menu (productCombo)
        String searchCategory = productCombo.getSelectionModel().getSelectedItem();
    
        // SQL query
        String selectSQL = "";
        PreparedStatement preparedStatement = null;
    
        try (Connection connection = ConnectionPool.getConnection()) {
            if (searchCategory == null || searchCategory.equals("show all")) {
                selectSQL = "SELECT * FROM products";
                preparedStatement = connection.prepareStatement(selectSQL);
            } 
            
            else if (searchCategory.equals("code") || searchCategory.equals("stock")) {
                selectSQL = "SELECT * FROM products WHERE " + searchCategory + " = ?";
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setInt(1, Integer.parseInt(searchText));
            } 
            
            else if (searchCategory.equals("price")) {
                selectSQL = "SELECT * FROM products WHERE CAST(" + searchCategory + " AS DECIMAL(10,2)) = ?";
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setDouble(1, Double.parseDouble(searchText.replace(",", "")));
            } 
            
            else {
                selectSQL = "SELECT * FROM products WHERE LOWER(" + searchCategory + ") LIKE ?";
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, "%" + searchText + "%");
            }
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    data.add(new product(resultSet.getInt("code"), 
                                         resultSet.getString("prod_name"), 
                                         resultSet.getString("category"),
                                         resultSet.getDouble("price"),
                                         resultSet.getInt("stock")));
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    
}
