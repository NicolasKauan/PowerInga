package com.poweringa.api.controllers;

import com.poweringa.api.dtos.ProdutoRequestDTO;
import com.poweringa.api.dtos.ProdutoResponseDTO;
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
    public ResponseEntity<ProdutoResponseDTO> save(@RequestBody ProdutoRequestDTO dto){
        ProdutoResponseDTO produtoSalvo = produtoService.save(dto);
        //Retorna o status 201(Criado) e o produto salvo no corpo da resposta .body
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    //Get /produtos
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> findAll(){
        //Api restfull devolvendo 200 de conseguiu encontrar
        return ResponseEntity.ok(produtoService.findAll());
    }

    //GET /produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable String id){
        return ResponseEntity.ok(produtoService.findById(id));
    }

    //DELETE /produtos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> delete(@PathVariable String id){
        produtoService.delete(id);
        //Status 204
        return ResponseEntity.noContent().build();
    }

    //PUT /produtos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> update(@PathVariable String id, @RequestBody ProdutoRequestDTO dto){
        try{
            ProdutoResponseDTO produtoAtualizado = produtoService.update(id, dto);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
