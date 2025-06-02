package com.fiap.safe_route.dto.resource;


import lombok.*;

public record ResourceRequest(
        @NonNull Long resourceTypeId,
        @NonNull Integer availableQuantity,
        @NonNull Long safePlaceId
) {
}