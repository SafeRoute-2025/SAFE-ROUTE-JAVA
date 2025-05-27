package com.fiap.safe_route.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GS2025_RESOURCE_TYPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESOURCE_TYPE")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;
}
