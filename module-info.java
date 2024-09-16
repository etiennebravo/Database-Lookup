module com.bravo {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    
    opens com.bravo to javafx.fxml, com.zaxxer.hikari;
    exports com.bravo;
}
