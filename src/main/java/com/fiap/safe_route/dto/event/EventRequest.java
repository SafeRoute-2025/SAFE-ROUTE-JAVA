package com.fiap.safe_route.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

public record EventRequest(
        @NotBlank
        String eventType,
        String description,
        @NotNull
        Date eventDate,
        @NotBlank
        String riskLevel,
        Double latitude,
        Double longitude
) {

}