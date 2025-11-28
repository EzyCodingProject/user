package com.EzyCoding.user.producer;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandleMessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private FanoutExchange fanoutExchange;

    public void sendHandle(String handle){
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", handle);
    }
}
