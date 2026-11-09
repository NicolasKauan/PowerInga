package com.poweringa.api.dtos;

import com.poweringa.api.models.ItensVenda;

import java.time.LocalDateTime;
import java.util.List;

public record VendaResponseDTO(
        String id,
        String id_usuario,
        List<ItensVenda> itens,
        Integer totalPontosVenda,
        LocalDateTime created_at
) {
}
