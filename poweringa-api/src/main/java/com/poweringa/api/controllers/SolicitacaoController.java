package com.poweringa.api.controllers;

import com.poweringa.api.models.Solicitacao;
import com.poweringa.api.models.User;
import com.poweringa.api.services.SolicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {
    @Autowired
    private SolicitacaoService solicitacaoService;

    @GetMapping
    public ResponseEntity<List<Solicitacao>> findAll(@AuthenticationPrincipal User usuarioLogado) {
        return ResponseEntity.ok(solicitacaoService.findAll(usuarioLogado));
    }

    @PostMapping
    public ResponseEntity<Solicitacao> create(
            @RequestBody Solicitacao solicitacao,
            @AuthenticationPrincipal User usuarioLogado
    ) {
        return ResponseEntity.ok(solicitacaoService.create(solicitacao, usuarioLogado));
    }
}
