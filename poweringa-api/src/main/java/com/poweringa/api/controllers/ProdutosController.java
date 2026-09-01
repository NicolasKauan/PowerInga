package com.poweringa.api.controllers;

import com.poweringa.api.models.ProdutosModel;
import com.poweringa.api.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {
    @Autowired
    private ProdutoService produtoService;

    // POST /produtos
    @PostMapping
    public ResponseEntity<ProdutosModel> save(@RequestBody ProdutosModel produto){
        ProdutosModel produtoSalvo = produtoService.save(produto);
        //Retorna o status 201(Criado) e o produto salvo no corpo da resposta .body
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    //Get /produtos
    @GetMapping
    public ResponseEntity<List<ProdutosModel>> findAll(){
        //Api restfull devolvendo 200 de conseguiu encontrar
        return ResponseEntity.ok(produtoService.findAll());
    }

    //GET /produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProdutosModel> findById(@PathVariable String id){
        return ResponseEntity.ok(produtoService.findById(id));
    }

    //DELETE /produtos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ProdutosModel> delete(@PathVariable String id){
        produtoService.delete(id);
        //Status 204
        return ResponseEntity.noContent().build();
    }

    //PUT /produtos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProdutosModel> update(@PathVariable String id, @RequestBody ProdutosModel produtosModel){
        try{
            ProdutosModel produto = produtoService.update(id, produtosModel);
            return ResponseEntity.ok(produto);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
