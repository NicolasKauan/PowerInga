package com.poweringa.api.dtos;

import com.poweringa.api.enums.CategoriaRecompensa;

public record SolicitacaoCreateDTO(String descricao, CategoriaRecompensa categoria) {
}
