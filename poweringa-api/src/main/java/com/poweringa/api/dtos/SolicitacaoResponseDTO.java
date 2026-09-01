package com.poweringa.api.dtos;

import com.poweringa.api.enums.SolicitacaoStatus;

public record SolicitacaoResponseDTO(String id, String descricao, SolicitacaoStatus status, String id_usuario) {
}
