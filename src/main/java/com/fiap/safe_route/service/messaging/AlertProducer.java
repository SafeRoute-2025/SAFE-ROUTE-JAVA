package com.fiap.safe_route.service.messaging;

import com.fiap.safe_route.dto.alert.AlertResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.fiap.safe_route.config.RabbitMQConfig;

@Service
public class AlertProducer {

    private final RabbitTemplate rabbitTemplate;

    public AlertProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAlert(AlertResponse alert) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ALERT_QUEUE, alert);
    }
}
