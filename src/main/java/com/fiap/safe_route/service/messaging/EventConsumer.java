package com.fiap.safe_route.service.messaging;

import com.fiap.safe_route.config.RabbitMQConfig;
import com.fiap.safe_route.dto.event.EventResponse;
import com.fiap.safe_route.service.EventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    private final EventService eventService;

    public EventConsumer(EventService eventService) {
        this.eventService = eventService;
    }

    @RabbitListener(queues = RabbitMQConfig.EVENT_QUEUE)
    public void receiveEvent(EventResponse event) {
        System.out.println("📩 Event received from queue:");
        System.out.println("→ Description: " + event.getDescription());
        eventService.saveOrUpdateFromMessage(event);
    }
}

