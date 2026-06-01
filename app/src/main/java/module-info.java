module com.agenda {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson; // Mantém o requires normalmente

    // O TRUQUE ESTÁ AQUI: Junte as duas bibliotecas separadas por vírgula!
    opens com.agenda to javafx.fxml, com.google.gson;

    exports com.agenda;
}