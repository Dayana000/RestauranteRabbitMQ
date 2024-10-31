package com.example.pcc_actualizado;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class PedidosController {
    @FXML
    private ListView<String> pedidosListView;

    @FXML
    private ListView<String> pedidosTomadosListView;

    @FXML
    private Button iniciarProductorButton;

    @FXML
    private Button iniciarConsumidorButton;

    @FXML
    private TextArea outputTextArea;

    private Restaurante restaurante;
    private Productor productor;
    private Consumidor consumidor;

    @FXML
    public void initialize() {
        restaurante = new Restaurante(3);
    }

    @FXML
    public void iniciarProductor() {
        try {
            productor = new Productor(restaurante, pedidosListView, 10, this);
            productor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void iniciarConsumidor() {
        try {
            consumidor = new Consumidor(restaurante, pedidosTomadosListView, this);
            consumidor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar el TextArea
    public void appendOutput(String message) {
        Platform.runLater(() -> {
            outputTextArea.appendText(message + "\n");
        });
    }
}
