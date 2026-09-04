package com.poweringa.api.controllers;

import com.poweringa.api.dtos.VendaCreateDTO;
import com.poweringa.api.models.User;
import com.poweringa.api.models.Venda;
import com.poweringa.api.services.VendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {
    @Autowired
    private VendaService vendaService;

    @GetMapping
    public ResponseEntity<List<Venda>> findAll(@AuthenticationPrincipal User usuarioLogado) {
        return ResponseEntity.ok(vendaService.findAll(usuarioLogado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> findById(@PathVariable String id, @AuthenticationPrincipal User usuarioLogado) {
        return ResponseEntity.ok(vendaService.findById(id, usuarioLogado));
    }

    @PostMapping
    public ResponseEntity<Venda> save(
            @RequestBody @Valid VendaCreateDTO dto,
            @AuthenticationPrincipal User usuarioLogado) {
        return ResponseEntity.ok(vendaService.save(dto, usuarioLogado));
    }
}
