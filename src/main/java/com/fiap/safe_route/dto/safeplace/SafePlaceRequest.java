package com.fiap.safe_route.dto.safeplace;
import jakarta.validation.constraints.*;
import lombok.*;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class SafePlaceRequest {
//    @NotBlank
//    private String name;
//
//    @NotBlank
//    private String address;
//
//    private Double latitude;
//    private Double longitude;
//
//    @NotNull
//    private Integer capacity;
//}

public record SafePlaceRequest(
        @NotBlank
        String name,
        @NotBlank
        String address,
        Double latitude,
        Double longitude,
        @NotNull
        int capacity
) {
}