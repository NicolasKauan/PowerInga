package com.poweringa.api.controllers;

import com.poweringa.api.models.Produtos;
import com.poweringa.api.repositories.ProdutosRepository;
import com.poweringa.api.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {
    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produtos>> findAll(){
        return ResponseEntity.ok(produtoService.findAll());
    }


}
