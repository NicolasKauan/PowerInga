package com.poweringa.api.dtos;

import com.poweringa.api.enums.Categorias;

public record CategoriaResponseDTO(String nome, int nivelImpacto) {
    public static CategoriaResponseDTO from(Categorias categorias) {
        return new CategoriaResponseDTO(categorias.name(), categorias.getNivelImpacto());
    }
}
