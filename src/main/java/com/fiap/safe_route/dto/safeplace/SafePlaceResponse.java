package com.fiap.safe_route.dto.safeplace;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SafePlaceResponse {
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer capacity;
}