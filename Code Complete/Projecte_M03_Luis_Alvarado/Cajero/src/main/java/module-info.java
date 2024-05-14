module com.cajero {
    requires javafx.controls;
    requires javafx.fxml;
    requires  java.sql; 
    requires javafx.swing;
    requires java.desktop; 
    requires javafx.base;
    opens com.cajero to javafx.fxml;
    exports com.cajero;
}
