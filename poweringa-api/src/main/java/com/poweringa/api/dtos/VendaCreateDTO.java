package com.poweringa.api.dtos;

import com.poweringa.api.models.ItensVenda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VendaCreateDTO(
        @Valid
        @NotEmpty
        List<VendaItemCreateDTO> itens
) {
}
