package com.fiap.safe_route.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GS2025_SAFE_PLACE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SAFE_PLACE")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "CAPACITY", nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "safePlace", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Resource> resources = new ArrayList<>();


}
