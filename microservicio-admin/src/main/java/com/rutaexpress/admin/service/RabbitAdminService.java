package com.rutaexpress.admin.service;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdminService {

    private final RabbitAdmin rabbitAdmin;

    public RabbitAdminService(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    public void createQueue(String queueName) {
        rabbitAdmin.declareQueue(new Queue(queueName, true));
    }

    public boolean deleteQueue(String queueName) {
        return rabbitAdmin.deleteQueue(queueName);
    }

    public void createExchange(String exchangeName, String type) {
        Exchange exchange = switch (type.toLowerCase()) {
            case "topic" -> new TopicExchange(exchangeName);
            case "fanout" -> new FanoutExchange(exchangeName);
            default -> new DirectExchange(exchangeName);
        };
        rabbitAdmin.declareExchange(exchange);
    }

    public boolean deleteExchange(String exchangeName) {
        return rabbitAdmin.deleteExchange(exchangeName);
    }

    public void createBinding(String queueName, String exchangeName, String routingKey) {
        Binding binding = BindingBuilder
                .bind(new Queue(queueName))
                .to(new DirectExchange(exchangeName))
                .with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    public void deleteBinding(String queueName, String exchangeName, String routingKey) {
        Binding binding = BindingBuilder
                .bind(new Queue(queueName))
                .to(new DirectExchange(exchangeName))
                .with(routingKey);
        rabbitAdmin.removeBinding(binding);
    }
}
