module com.agenda {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.agenda to javafx.fxml;
    exports com.agenda;
}