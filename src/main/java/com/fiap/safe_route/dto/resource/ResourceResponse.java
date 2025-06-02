package com.fiap.safe_route.dto.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {
    private Long id;
    private String resourceType;
    private Integer availableQuantity;
    private Long safePlaceId;
    private String safePlaceName;
}