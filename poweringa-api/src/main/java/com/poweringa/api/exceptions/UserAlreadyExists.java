package com.poweringa.api.exceptions;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists() {
        super("ERRO: Usuário já existente!");
    }
}
