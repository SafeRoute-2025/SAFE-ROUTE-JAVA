package com.fiap.safe_route.dto.event;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private Long id;
    private String eventType;
    private String description;
    private Date eventDate;
    private String riskLevel;
    private Double latitude;
    private Double longitude;
}