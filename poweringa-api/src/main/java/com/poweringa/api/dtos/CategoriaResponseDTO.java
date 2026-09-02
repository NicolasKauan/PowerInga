package com.poweringa.api.dtos;

import com.poweringa.api.enums.CategoriaRecompensa;

public record CategoriaResponseDTO(String nome, int nivelImpacto) {
    public static CategoriaResponseDTO from(CategoriaRecompensa categoriaRecompensa) {
        return new CategoriaResponseDTO(categoriaRecompensa.name(), categoriaRecompensa.getNivelImpacto());
    }
}
