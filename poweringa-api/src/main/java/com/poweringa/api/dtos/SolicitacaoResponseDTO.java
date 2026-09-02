package com.poweringa.api.dtos;

import com.poweringa.api.enums.CategoriaRecompensa;
import com.poweringa.api.enums.SolicitacaoStatus;

//adicionado o campo para a categoria na solicitação
public record SolicitacaoResponseDTO(String id, String descricao, SolicitacaoStatus status, String id_usuario, CategoriaRecompensa categoria) {
}
