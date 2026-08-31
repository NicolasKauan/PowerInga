package com.poweringa.api.dtos;

import com.poweringa.api.enums.Categorias;
import com.poweringa.api.enums.SolicitacaoStatus;

public record SolicitacaoResponseDTO(String descricao, SolicitacaoStatus solicitacaoStatus, Categorias categoria) {
}
