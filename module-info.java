module com.bravo {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.bravo to javafx.fxml;
    exports com.bravo;
}
