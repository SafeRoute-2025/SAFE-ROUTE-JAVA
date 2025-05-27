package com.fiap.safe_route.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,
        @NotBlank(message = "O Email é obrigatório")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "O email deve ser válido")
        String email,
        @NotBlank(message = "A senha é obrigatória")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "A senha deve conter no mínimo 8 caracteres, uma letra maiúscula, uma letra minúscula, um número e um caractere especial"
        )
        String password,
        @NotBlank(message = "O telefone é obrigatório")
        //telefone tem que ser no formato 9999999999 ou 99999999999
        @Pattern(regexp = "^\\d{10,11}$", message = "O telefone deve conter 10 ou 11 dígitos (DDD + número)")
        String phone
) {
}
