package com.poweringa.api.config;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

// A chave assina os tokens com HMAC-SHA256; uma chave curta ou ausente permitiria forjar tokens.
// A validação faz a API se recusar a iniciar nesses casos.
@Validated
@ConfigurationProperties("api.security.token")
public record JwtProperties(
        @NotNull(message = JwtProperties.INVALID_SECRET_KEY_MESSAGE)
        @Size(min = JwtProperties.MINIMUM_SECRET_KEY_LENGTH, message = JwtProperties.INVALID_SECRET_KEY_MESSAGE)
        String secretKey
) {
    private static final int MINIMUM_SECRET_KEY_LENGTH = 32;

    private static final String INVALID_SECRET_KEY_MESSAGE = "ERRO: Defina a variável de ambiente JWT_SECRET_KEY com pelo menos "
            + MINIMUM_SECRET_KEY_LENGTH + " caracteres (veja o .env.example).";
}
