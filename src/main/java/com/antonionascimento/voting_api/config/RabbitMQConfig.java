package com.antonionascimento.voting_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;

@Configuration
public class RabbitMQConfig {

    private static final String MAIN_QUEUE_NAME = "voting-queue";
    private static final String DLX_QUEUE_NAME = "voting-retry-queue"; 
    private static final String EXCHANGE_NAME = "myExchange";
    private static final String DLX_EXCHANGE_NAME = "dlx-exchange"; 
    private static final String ROUTING_KEY = "voting.vote.submitted";
    private static final String DLX_ROUTING_KEY = "voting-retry";

    @Bean
    public Queue mainQueue(){
        return QueueBuilder.durable(MAIN_QUEUE_NAME)
        .withArgument("x-dead-letter-exchange", DLX_EXCHANGE_NAME)
        .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
        .build();
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(DLX_QUEUE_NAME)
                .withArgument("x-message-ttl", 5000)
                .withArgument("x-dead-letter-exchange", EXCHANGE_NAME) 
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY) 
                .build();
    }

    @Bean
    public DirectExchange mainExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE_NAME);
    }

    @Bean 
    public Binding mainBinding(Queue mainQueue, DirectExchange mainExchange) {
        return BindingBuilder.bind(mainQueue).to(mainExchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding dlxBinding(Queue retryQueue, DirectExchange dlxExchange) {
        return BindingBuilder.bind(retryQueue).to(dlxExchange).with(DLX_ROUTING_KEY);
    }

}
