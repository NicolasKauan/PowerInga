package com.poweringa.api.dtos;

import com.poweringa.api.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO(
        @NotBlank(message = "ERRO: E-mail é obrigatório!")
        @Email(message = "ERRO: E-mail inválido!")
        String email,

        @NotBlank(message = "ERRO: Senha é obrigatória!")
        String senha,

        String descricao,

        @NotNull(message = "ERRO: Cargo é obrigatório!")
        UserRole cargo
) {
}
