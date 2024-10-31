package com.example.pcc_actualizado;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQService {
    private static final String QUEUE_NAME = "pedidos";
    private static RabbitMQService instance;
    private Connection connection;
    private Channel channel;
    private PedidosController controller;

    // Constructor privado para el patrón Singleton
    private RabbitMQService() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");


        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    }

    // Método para obtener la instancia única de RabbitMQService
    public static synchronized RabbitMQService getInstance() throws Exception {
        if (instance == null) {
            instance = new RabbitMQService();
        }
        return instance;
    }

    public Channel getChannel() {
        return channel;
    }

    // Método para enviar un mensaje a la cola
    public void sendMessage(String message) throws Exception {
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println("Mensaje enviado a RabbitMQ: " + message);
    }

    // Método para cerrar conexión y canal
    public void close() throws Exception {
        if (channel != null && channel.isOpen()) channel.close();
        if (connection != null && connection.isOpen()) connection.close();
    }
}
