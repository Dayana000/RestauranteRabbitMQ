package com.example.pcc_actualizado;

import com.rabbitmq.client.*;
import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.io.IOException;

public class Consumidor extends Thread {
    private final Restaurante restaurante;
    private final ListView<String> pedidosTomadosListView;
    private final RabbitMQService rabbitMQService;
    private final PedidosController controller;  // Añadir esto

    public Consumidor(Restaurante restaurante, ListView<String> pedidosTomadosListView, PedidosController controller) throws Exception {
        this.restaurante = restaurante;
        this.pedidosTomadosListView = pedidosTomadosListView;
        this.rabbitMQService = RabbitMQService.getInstance();
        this.controller = controller;  // Inicializar el controlador
    }

    @Override
    public void run() {
        try {
            rabbitMQService.getChannel().basicConsume("pedidos", false, new DefaultConsumer(rabbitMQService.getChannel()) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String mensaje = new String(body, "UTF-8");
                    controller.appendOutput("Pedido recibido: " + mensaje);  // Agregar a la salida
                    try {
                        String pedidoTomado = restaurante.tomarPedido();
                        Platform.runLater(() -> pedidosTomadosListView.getItems().add(pedidoTomado));
                        Thread.sleep(3000);
                        rabbitMQService.getChannel().basicAck(envelope.getDeliveryTag(), false);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
