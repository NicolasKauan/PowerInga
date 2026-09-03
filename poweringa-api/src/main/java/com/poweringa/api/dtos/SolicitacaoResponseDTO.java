package com.poweringa.api.dtos;

import com.poweringa.api.enums.CategoriaRecompensa;
import com.poweringa.api.enums.SolicitacaoStatus;

public record SolicitacaoResponseDTO(
        String id,
        String descricao,
        CategoriaRecompensa categoria,
        SolicitacaoStatus status,
        String id_usuario
) {}
