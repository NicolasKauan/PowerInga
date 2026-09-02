package com.poweringa.api.services;

import com.poweringa.api.models.ProdutosModel;
import com.poweringa.api.repositories.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutosRepository produtosRepository;

    //Get /produtos no service
    public List<ProdutosModel> findAll(){
        return produtosRepository.findAll();
    }

    //GET /produtos/{id}
    public ProdutosModel findById(String id){
        //Retorna o produto pelo id ou um erro
        return produtosRepository.findById(id)
                //Como existe uma chance do ID não existir, temos esse
                // filtro para o Java lançar um erro automático para o produto não encontrado
                .orElseThrow( () -> new RuntimeException("Produto não encontrado!"));
    }

    //POST /produtos
    public ProdutosModel save(ProdutosModel produto){
        return produtosRepository.save(produto);
    }

    //DELETE /produtos/id
    public void delete(String id){
        this.findById(id);//Primeiro verifica se existe. Se não existir, já lançando o erro, reaproveitamento de código
        produtosRepository.deleteById(id);//Se passou pela linha 37 apagar normalmente
    }

    //PUT /produtos/{id}
    public ProdutosModel update(String id, ProdutosModel produto){
        ProdutosModel model = this.findById(id);//Mesma coisa do delete, verifica se existe. Se não existir, temos um erro personalizado para isso.
        model.setDescricao(produto.getDescricao());
        model.setValorPontos(produto.getValorPontos());
        return produtosRepository.save(model);
    }
}
