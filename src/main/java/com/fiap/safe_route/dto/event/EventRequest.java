package com.fiap.safe_route.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;


public record EventRequest(
        @NotBlank(message = "Event type cannot be blank")
        String eventType,

        String description,

        Date eventDate,

        @NotBlank(message = "Risk level cannot be blank")
        String riskLevel,

        Double latitude,

        Double longitude
) {
}