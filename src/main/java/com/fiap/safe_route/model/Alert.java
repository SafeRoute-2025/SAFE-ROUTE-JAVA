package com.fiap.safe_route.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "GS2025_ALERT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALERT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_EVENT", nullable = false)
    private Event event;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Column(name = "SENT_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;
}
