package com.poweringa.api.dtos;

import com.poweringa.api.enums.UserRole;

public record RegisterRequestDTO(String email, String senha, String descricao, UserRole cargo) {
}
