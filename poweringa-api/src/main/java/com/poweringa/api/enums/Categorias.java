package com.poweringa.api.enums;

public enum Categorias {
    MICRO_ACAO (1),
    ACAO_LOCALIZADA (2),
    ACAO_ESTRUTURAL (3),
    ACAO_ALTO_IMPACTO (4),
    ACAO_GLOBAL (5);

    private final int nivelImpacto;

    Categorias(int nivelImpacto) {
        this.nivelImpacto = nivelImpacto;
    }

    public int getNivelImpacto() {
        return nivelImpacto;
    }
}
