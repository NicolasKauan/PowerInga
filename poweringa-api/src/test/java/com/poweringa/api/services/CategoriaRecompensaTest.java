package com.poweringa.api.services;

import com.poweringa.api.enums.CategoriaRecompensa;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoriaRecompensaTest {
    @Test
    void deveConterCincoCategoriasEmOrdem(){
        CategoriaRecompensa[] categoria = CategoriaRecompensa.values();

        assertEquals(5, categoria.length);
        assertEquals(CategoriaRecompensa.MICRO_ACAO, categoria[0]);
        assertEquals(CategoriaRecompensa.ACAO_LOCALIZADA, categoria[1]);
        assertEquals(CategoriaRecompensa.ACAO_ESTRUTURAL, categoria[2]);
        assertEquals(CategoriaRecompensa.ACAO_ALTO_IMPACTO, categoria[3]);
        assertEquals(CategoriaRecompensa.ACAO_GLOBAL, categoria[4]);
    }
    @Test
    void cadaCategoriaDeveTerONivelDeImpactoCorreto() {
        assertEquals(1, CategoriaRecompensa.MICRO_ACAO.getNivelImpacto());
        assertEquals(2, CategoriaRecompensa.ACAO_LOCALIZADA.getNivelImpacto());
        assertEquals(3, CategoriaRecompensa.ACAO_ESTRUTURAL.getNivelImpacto());
        assertEquals(4, CategoriaRecompensa.ACAO_ALTO_IMPACTO.getNivelImpacto());
        assertEquals(5, CategoriaRecompensa.ACAO_GLOBAL.getNivelImpacto());
    }

    @Test
    void cadaCategoriaDeveTerOsPontosCorretos() {
        assertEquals(15, CategoriaRecompensa.MICRO_ACAO.getPontos());
        assertEquals(30, CategoriaRecompensa.ACAO_LOCALIZADA.getPontos());
        assertEquals(60, CategoriaRecompensa.ACAO_ESTRUTURAL.getPontos());
        assertEquals(120, CategoriaRecompensa.ACAO_ALTO_IMPACTO.getPontos());
        assertEquals(240, CategoriaRecompensa.ACAO_GLOBAL.getPontos());
    }

    @Test
    void pontosDevemCrescerConformeNivelDeImpactoAumenta() {
        CategoriaRecompensa[] categorias = CategoriaRecompensa.values();

        for (int i = 1; i < categorias.length; i++) {
            int pontosAnterior = categorias[i - 1].getPontos();
            int pontosAtual = categorias[i].getPontos();

            assertEquals(true, pontosAtual > pontosAnterior);
        }
    }
}
