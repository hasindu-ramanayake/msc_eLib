package com.searchService.searchService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "itemQueue";
    public static final String EXCHANGE = "itemExchange";
    public static final String ROUTING_KEY = "item.routingKey";

    // Queue
    @Bean
    public Queue itemQueue() {
        return new Queue(QUEUE, true);
    }

    // Exchange
    @Bean
    public DirectExchange itemExchange() {
        return new DirectExchange(EXCHANGE);
    }

    // Binding
    @Bean
    public Binding binding(Queue itemQueue, DirectExchange itemExchange) {
        return BindingBuilder
                .bind(itemQueue)
                .to(itemExchange)
                .with(ROUTING_KEY);
    }

    // JSON Converter
    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}