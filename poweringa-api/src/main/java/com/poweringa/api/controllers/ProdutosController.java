package com.poweringa.api.controllers;

import com.poweringa.api.models.Produtos;
import com.poweringa.api.repositories.ProdutosRepository;
import com.poweringa.api.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {
    @Autowired
    private ProdutoService produtoService;

    // POST /produtos
    @PostMapping
    public ResponseEntity<Produtos> save(@RequestBody Produtos produto){
        Produtos produtoSalvo = produtoService.save(produto);
        //Retorna o status 201(Criado) e o produto salvo no corpo da resposta .body
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    //Get /produtos
    @GetMapping
    public ResponseEntity<List<Produtos>> findAll(){
        //Api restfull devolvendo 200 de conseguiu encontrar
        return ResponseEntity.ok(produtoService.findAll());
    }

    //GET /produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Produtos> findById(@PathVariable String id){
        return ResponseEntity.ok(produtoService.findById(id));
    }

    //DELETE /produtos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Produtos> delete(@PathVariable String id){
        //Status 204 
        return ResponseEntity.noContent().build();
    }
}
