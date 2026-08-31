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

    public List<Produtos> findAll(){
        return produtosRepository.findAll();
    }

}
