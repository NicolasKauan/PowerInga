package com.poweringa.api.controllers;

import com.poweringa.api.models.Solicitacao;
import com.poweringa.api.models.User;
import com.poweringa.api.services.SolicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {
    @Autowired
    private SolicitacaoService solicitacaoService;

    @PostMapping
    public ResponseEntity<Solicitacao> create(
            @RequestBody Solicitacao solicitacao,
            @AuthenticationPrincipal User usuarioLogado
    ) {
        return ResponseEntity.ok(solicitacaoService.create(solicitacao, usuarioLogado));
    }
}
