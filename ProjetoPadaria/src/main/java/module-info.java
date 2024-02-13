module com.example.projetopadaria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.projetopadaria to javafx.fxml;
    exports com.example.projetopadaria;
}