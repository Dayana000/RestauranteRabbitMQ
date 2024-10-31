package com.example.pcc_actualizado;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Restaurante {
    private final Queue<String> pedidos = new LinkedList<>();
    private final Semaphore semaforo;

    public Restaurante(int maxConsumidores) {
        this.semaforo = new Semaphore(maxConsumidores);
    }

    public void agregarPedido(String pedido) {
        synchronized (pedidos) {
            pedidos.add(pedido);
            System.out.println("Chef: Pedido listo - " + pedido);
            pedidos.notifyAll();
        }
    }

    public String tomarPedido() throws InterruptedException {
        semaforo.acquire();
        try {
            synchronized (pedidos) {
                while (pedidos.isEmpty()) {
                    pedidos.wait();
                }
                String pedido = pedidos.poll();
                System.out.println("Repartidor: Pedido tomado - " + pedido);
                return pedido;
            }
        } finally {
            semaforo.release();
        }
    }

}
