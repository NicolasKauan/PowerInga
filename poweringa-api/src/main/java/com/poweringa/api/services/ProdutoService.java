package com.poweringa.api.services;

import com.poweringa.api.models.Produtos;
import com.poweringa.api.repositories.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutosRepository produtosRepository;

    //Get /produtos no service
    public List<Produtos> findAll(){
        return produtosRepository.findAll();
    }

    //GET /produtos/{id}
    public Produtos findById(String id){
        //Retorna o produto pelo id ou um erro
        return produtosRepository.findById(id)
                //Como existe uma chance do ID não existir, temos esse
                // filtro para o Java lançar um erro automático para o produto não encontrado
                .orElseThrow( () -> new RuntimeException("Produto não encontrado!"));
    }

    //POST /produtos
    public Produtos save(Produtos produto){
        return produtosRepository.save(produto);
    }

    //DELETE /produtos/id
    public void delete(String id){
        produtosRepository.deleteById(id);
    }

}
