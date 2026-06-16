module com.agenda {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.google.gson; 

    opens com.agenda to javafx.fxml, com.google.gson;

    exports com.agenda;
}