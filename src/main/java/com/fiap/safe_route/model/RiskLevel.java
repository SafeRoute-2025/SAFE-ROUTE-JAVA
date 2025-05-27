package com.fiap.safe_route.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GS2025_RISK_LEVEL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RISK_LEVEL")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
}
