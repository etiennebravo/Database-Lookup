package com.bravo;

import java.io.IOException;
import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    private void switchToSearch() throws IOException {
        App.setRoot("search");
    }

    @FXML
    private void switchToModify() throws IOException {
        App.setRoot("modify");
    }

    @FXML
    private void switchToDelete() throws IOException {
        App.setRoot("delete");
    }

    @FXML
    private void switchToInsert() throws IOException {
        App.setRoot("insert");
    }
}