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

    /** Durable queue for user lifecycle events. */
    @Bean
    public Queue userEventQueue() {
        return new Queue(userQueue, true);
    }

    /** Topic exchange for user lifecycle events. */
    @Bean
    public TopicExchange userEventExchange() {
        return new TopicExchange(userExchange);
    }

    /** Binds the user queue to the user exchange. */
    @Bean
    public Binding userEventBinding(Queue userEventQueue, TopicExchange userEventExchange) {
        return BindingBuilder
                .bind(userEventQueue)
                .to(userEventExchange)
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
