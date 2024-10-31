package com.example.pcc_actualizado;

import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Productor extends Thread {
    private final Restaurante restaurante;
    private final ListView<String> pedidosListView;
    private final RabbitMQService rabbitMQService;
    private final int maxPedidos;
    private final List<String> platosVariados;
    private final Random random;
    private final PedidosController controller;  // Añadir esto

    public Productor(Restaurante restaurante, ListView<String> pedidosListView, int maxPedidos, PedidosController controller) throws Exception {
        this.restaurante = restaurante;
        this.pedidosListView = pedidosListView;
        this.rabbitMQService = RabbitMQService.getInstance();
        this.maxPedidos = maxPedidos;
        this.platosVariados = Arrays.asList("Ensalada", "Hamburguesa", "Papas", "Perros calientes", "Salchipapa");
        this.random = new Random();
        this.controller = controller;  // Inicializar el controlador
    }

    @Override
    public void run() {
        try {
            int numeroPedido = 1;
            while (numeroPedido <= maxPedidos) {
                String plato = platosVariados.get(random.nextInt(platosVariados.size()));
                String pedido = "Pedido #" + numeroPedido + ": " + plato;
                restaurante.agregarPedido(pedido);
                rabbitMQService.sendMessage(pedido);

                // Actualiza la interfaz gráfica
                Platform.runLater(() -> pedidosListView.getItems().add(pedido));
                controller.appendOutput("Chef: Pedido listo - " + pedido);  // Agregar a la salida

                numeroPedido++;
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
