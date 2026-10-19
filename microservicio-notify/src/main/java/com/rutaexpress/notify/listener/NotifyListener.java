package com.rutaexpress.notify.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotifyListener {

    private static final Logger log = LoggerFactory.getLogger(NotifyListener.class);

    @RabbitListener(queues = "q.cmd.email")
    public void consumeEmail(Map<String, Object> payload) {
        log.info("--> [q.cmd.email] [ACK] Notificación de correo recibida: {}", payload);
        if (Boolean.TRUE.equals(payload.get("simularError"))) {
            log.error("--> [q.cmd.email] Error forzado. Redirigiendo a DLQ...");
            throw new RuntimeException("Fallo intencional en correo");
        }
    }

    @RabbitListener(queues = "q.cmd.warehouse")
    public void consumeWarehouse(Map<String, Object> payload) {
        log.info("--> [q.cmd.warehouse] [ACK] Ticket de bodega / picking recibido: {}", payload);
    }

    @RabbitListener(queues = "q.cmd.label")
    public void consumeLabel(Map<String, Object> payload) {
        log.info("--> [q.cmd.label] [ACK] Solicitud de guía de despacho / etiqueta recibida: {}", payload);
    }
}
