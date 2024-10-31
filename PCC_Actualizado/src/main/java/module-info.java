module com.example.pcc_actualizado {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.rabbitmq.client;


    opens com.example.pcc_actualizado to javafx.fxml;
    exports com.example.pcc_actualizado;
}
