package com.poweringa.api.enums;

public enum CategoriaRecompensa {
    MICRO_ACAO (1, 15),
    ACAO_LOCALIZADA (2, 30),
    ACAO_ESTRUTURAL (3, 60),
    ACAO_ALTO_IMPACTO (4, 120),
    ACAO_GLOBAL (5, 240);

    private final int nivelImpacto;
    private final int pontos;

    CategoriaRecompensa(int nivelImpacto, int  pontos) {
        this.nivelImpacto = nivelImpacto;
        this.pontos = pontos;
    }

    public int getNivelImpacto() {
        return nivelImpacto;
    }

    public int getPontos() {
        return pontos;
    }
}
