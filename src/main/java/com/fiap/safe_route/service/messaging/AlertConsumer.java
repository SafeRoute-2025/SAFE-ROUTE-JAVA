package com.fiap.safe_route.service.messaging;

import com.fiap.safe_route.dto.alert.AlertResponse;
import com.fiap.safe_route.service.AlertService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AlertConsumer {

    private final AlertService alertService;

    public AlertConsumer(AlertService alertService) {
        this.alertService = alertService;
    }

    @RabbitListener(queues = "alert.queue")
    public void receiveMessage(AlertResponse response) {
        System.out.println("📩 Alert received from queue:");
        System.out.println("→ Message: " + response.getMessage());
        alertService.saveOrUpdateFromMessage(response);
    }
}
