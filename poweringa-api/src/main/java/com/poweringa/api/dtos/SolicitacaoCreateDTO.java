package com.poweringa.api.dtos;

import com.poweringa.api.enums.CategoriaRecompensa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoCreateDTO(
        @NotBlank(message = "ERRO: Descrição da solicitação é obrigatória!")
        String descricao,

        @NotNull(message = "ERRO: Categoria é obrigatória!")
        CategoriaRecompensa categoria
) {
}
