package com.example.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.notification}")
    private String queue;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Value("${rabbitmq.user.queue}") // #JF
    private String userQueue; // #JF

    // Déclare la queue — RabbitMQ la crée automatiquement si elle n'existe pas
    @Bean
    public Queue notificationQueue() {
        return new Queue(queue, true); // true = durable, survive au redémarrage
    }

    // #JF — user lifecycle events queue published by user-service
    @Bean
    public Queue userEventQueue() { // #JF
        return new Queue(userQueue, true); // #JF
    } // #JF

    // Déclare l'exchange de type Topic
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(exchange);
    }

    // Lie la queue à l'exchange via la routing key
    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(routingKey);
    }

    // Convertit automatiquement les messages en JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Utilise le convertisseur JSON pour envoyer et recevoir
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}