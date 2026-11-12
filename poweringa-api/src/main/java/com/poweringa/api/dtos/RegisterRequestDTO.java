package com.poweringa.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// O cargo não vem do cliente: o cadastro público sempre cria CLIENTE (ver AuthService.register).
public record RegisterRequestDTO(
        @NotBlank(message = "ERRO: E-mail é obrigatório!")
        @Email(message = "ERRO: E-mail inválido!")
        String email,

        @NotBlank(message = "ERRO: Senha é obrigatória!")
        String senha,

        @NotBlank(message = "ERRO: Nome é obrigatório!")
        String descricao
) {
}
