package com.fiap.safe_route.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {
    private Long id;
    private String message;
    private LocalDateTime sentAt;
    private Long eventId;
    private String event;

    public String getFormattedDate() {
        if (sentAt == null) return "";
        return sentAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}

