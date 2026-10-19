package com.rutaexpress.notify.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("cmd.direct");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("cmd.topic");
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("cmd.dead.dlx");
    }

    // --- 1. EMAIL ---
    @Bean
    public Queue emailDLQ() {
        return QueueBuilder.durable("q.cmd.email.dlq").build();
    }

    @Bean
    public Queue emailQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "cmd.dead.dlx");
        args.put("x-dead-letter-routing-key", "q.cmd.email.dlq");
        return QueueBuilder.durable("q.cmd.email").withArguments(args).build();
    }

    @Bean
    public Binding emailDirectBinding() {
        return BindingBuilder.bind(emailQueue()).to(directExchange()).with("email.send");
    }

    @Bean
    public Binding emailTopicBinding() {
        return BindingBuilder.bind(emailQueue()).to(topicExchange()).with("email.*");
    }

    @Bean
    public Binding emailDLQBinding() {
        return BindingBuilder.bind(emailDLQ()).to(deadLetterExchange()).with("q.cmd.email.dlq");
    }

    // --- 2. WAREHOUSE ---
    @Bean
    public Queue warehouseDLQ() {
        return QueueBuilder.durable("q.cmd.warehouse.dlq").build();
    }

    @Bean
    public Queue warehouseQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "cmd.dead.dlx");
        args.put("x-dead-letter-routing-key", "q.cmd.warehouse.dlq");
        return QueueBuilder.durable("q.cmd.warehouse").withArguments(args).build();
    }

    @Bean
    public Binding warehouseDirectBinding() {
        return BindingBuilder.bind(warehouseQueue()).to(directExchange()).with("warehouse.ticket");
    }

    @Bean
    public Binding warehouseTopicBinding() {
        return BindingBuilder.bind(warehouseQueue()).to(topicExchange()).with("warehouse.#");
    }

    @Bean
    public Binding warehouseDLQBinding() {
        return BindingBuilder.bind(warehouseDLQ()).to(deadLetterExchange()).with("q.cmd.warehouse.dlq");
    }

    // --- 3. LABEL ---
    @Bean
    public Queue labelDLQ() {
        return QueueBuilder.durable("q.cmd.label.dlq").build();
    }

    @Bean
    public Queue labelQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "cmd.dead.dlx");
        args.put("x-dead-letter-routing-key", "q.cmd.label.dlq");
        return QueueBuilder.durable("q.cmd.label").withArguments(args).build();
    }

    @Bean
    public Binding labelDirectBinding() {
        return BindingBuilder.bind(labelQueue()).to(directExchange()).with("label.gen");
    }

    @Bean
    public Binding labelTopicBinding() {
        return BindingBuilder.bind(labelQueue()).to(topicExchange()).with("label.*");
    }

    @Bean
    public Binding labelDLQBinding() {
        return BindingBuilder.bind(labelDLQ()).to(deadLetterExchange()).with("q.cmd.label.dlq");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
