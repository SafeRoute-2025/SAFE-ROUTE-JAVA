package com.fiap.safe_route.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GS2025_RESOURCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESOURCE")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_RESOURCE_TYPE", nullable = false)
    private ResourceType resourceType;

    @Column(name = "AVAILABLE_QUANTITY", nullable = false)
    private Integer availableQuantity;

    @ManyToOne
    @JoinColumn(name = "ID_SAFE_PLACE", nullable = false)
    private SafePlace safePlace;
}
