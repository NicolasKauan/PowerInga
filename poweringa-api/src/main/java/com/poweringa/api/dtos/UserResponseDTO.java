package com.poweringa.api.dtos;

import com.poweringa.api.enums.UserRole;

public record UserResponseDTO(String id, String email, String descricao, UserRole cargo, Integer pontos) {
}
