package com.poweringa.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VendaItemCreateDTO(@NotBlank String idProduto, @NotNull @Positive Integer quantidade) {
}
