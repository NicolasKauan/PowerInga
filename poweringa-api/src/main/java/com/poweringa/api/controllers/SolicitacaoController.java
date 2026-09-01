package com.poweringa.api.controllers;

import com.poweringa.api.dtos.SolicitacaoCreateDTO;
import com.poweringa.api.dtos.SolicitacaoResponseDTO;
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
    public ResponseEntity<List<SolicitacaoResponseDTO>> findAll(@AuthenticationPrincipal User usuarioLogado) {
        return ResponseEntity.ok(solicitacaoService.findAll(usuarioLogado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> findById(@PathVariable String id, @AuthenticationPrincipal User usuarioLogado){
        return ResponseEntity.ok(solicitacaoService.findById(id, usuarioLogado));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> create(
            @RequestBody SolicitacaoCreateDTO dto,
            @AuthenticationPrincipal User usuarioLogado
    ) {
        return ResponseEntity.ok(solicitacaoService.create(dto, usuarioLogado));
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<SolicitacaoResponseDTO> approve(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(solicitacaoService.approve(id));
    }

    @PatchMapping("/{id}/reprovar")
    public ResponseEntity<SolicitacaoResponseDTO> reject(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(solicitacaoService.reject(id));
    }
}
