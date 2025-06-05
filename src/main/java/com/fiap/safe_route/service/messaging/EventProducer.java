package com.fiap.safe_route.service.messaging;

import com.fiap.safe_route.config.RabbitMQConfig;
import com.fiap.safe_route.dto.event.EventResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    private final RabbitTemplate rabbitTemplate;

    public EventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEvent(EventResponse event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_QUEUE, event);
    }
}
