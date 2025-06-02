package com.fiap.safe_route.dto.alert;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record AlertRequest(
        @NotNull(message = "Event ID cannot be null")
        Long eventId,

        @NotBlank(message = "Message cannot be blank")
        String message,

        LocalDateTime sentAt
) {
}
