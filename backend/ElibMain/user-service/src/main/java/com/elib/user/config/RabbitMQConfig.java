package com.elib.user.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.user.exchange}")
    private String userExchange;

    @Value("${rabbitmq.user.queue}")
    private String userQueue;

    @Value("${rabbitmq.user.routing-key}")
    private String userRoutingKey;

    /** Durable queue for notification events (aligned with NotificationService). */
    @Bean
    public Queue notificationEventQueue() {
        return new Queue(userQueue, true);
    }

    /** Topic exchange for notification events (aligned with NotificationService). */
    @Bean
    public TopicExchange notificationEventExchange() {
        return new TopicExchange(userExchange);
    }

    /** Binds the notification queue to the notification exchange. */
    @Bean
    public Binding notificationEventBinding(Queue notificationEventQueue, TopicExchange notificationEventExchange) {
        return BindingBuilder
                .bind(notificationEventQueue)
                .to(notificationEventExchange)
                .with(userRoutingKey);
    }

    /** Converts messages to/from JSON automatically. */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /** Configures the RabbitTemplate to use JSON serialisation. */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
