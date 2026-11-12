package com.poweringa.api.exceptions;

public class BusinessRuleViolation extends RuntimeException {
    public BusinessRuleViolation(String message) {
        super(message);
    }
}
