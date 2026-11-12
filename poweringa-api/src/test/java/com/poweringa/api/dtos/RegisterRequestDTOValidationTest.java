package com.poweringa.api.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterRequestDTOValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "   ")
    void deveRejeitarCadastroSemNome(String nomeInvalido) {
        RegisterRequestDTO request = new RegisterRequestDTO("teste@teste.com", "123", nomeInvalido);

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(request);

        List<String> descricaoMessages = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("descricao"))
                .map(ConstraintViolation::getMessage)
                .toList();

        assertEquals(List.of("ERRO: Nome é obrigatório!"), descricaoMessages);
    }

    @Test
    void deveAceitarCadastroComTodosOsCamposValidos() {
        RegisterRequestDTO request = new RegisterRequestDTO("teste@teste.com", "123", "Maria");

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}
