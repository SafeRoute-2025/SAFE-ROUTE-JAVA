package com.fiap.safe_route.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "GS2025_EVENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EVENT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_EVENT_TYPE", nullable = false)
    private EventType eventType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EVENT_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;

    @ManyToOne
    @JoinColumn(name = "ID_RISK_LEVEL", nullable = false)
    private RiskLevel riskLevel;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;
}
