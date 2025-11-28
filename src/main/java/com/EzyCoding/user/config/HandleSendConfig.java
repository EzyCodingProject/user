package com.EzyCoding.user.config;


import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandleSendConfig {
    @Value("${spring.rabbitmq.exchange.handle}")
    private String handleExchange;

    @Bean
    public FanoutExchange fanoutExchange() {return new FanoutExchange(handleExchange);}
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {return new Jackson2JsonMessageConverter();}
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
