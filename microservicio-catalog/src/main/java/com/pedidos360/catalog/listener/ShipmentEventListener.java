package com.pedidos360.catalog.listener;

import com.pedidos360.catalog.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ShipmentEventListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleShipmentCreated(Map<String, Object> shipmentData) {
        System.out.println("--> [RabbitMQ ms-catalog] EVENTO RECIBIDO CON ÉXITO: " + shipmentData);
    }
}
