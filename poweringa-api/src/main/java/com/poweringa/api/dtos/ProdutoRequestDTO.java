package com.poweringa.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdutoRequestDTO(
        @NotBlank(message = "ERRO: Descrição do produto é obrigatória!")
        String descricao,

        @NotNull(message = "ERRO: Valor em pontos é obrigatório!")
        @Positive(message = "ERRO: Valor em pontos deve ser maior que zero!")
        Integer valorPontos
) {
}
