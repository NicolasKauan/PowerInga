package com.poweringa.api.services;

import com.poweringa.api.dtos.ProdutoRequestDTO;
import com.poweringa.api.dtos.ProdutoResponseDTO;
import com.poweringa.api.models.ProdutosModel;
import com.poweringa.api.repositories.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutosRepository produtosRepository;

    //Um método auxiliar de conversão ou os mappers
    public ProdutosModel toEntity(ProdutoRequestDTO dto) {
        ProdutosModel model = new ProdutosModel();
        model.setDescricao(dto.descricao());
        model.setValorPontos(dto.valorPontos());
        return model;
    }

    public ProdutoResponseDTO toDTO(ProdutosModel model){
        return new ProdutoResponseDTO(model.getId(), model.getDescricao(), model.getValorPontos());
    }


    //Get /produtos no service
    public List<ProdutoResponseDTO> findAll(){
        return produtosRepository.findAll().stream()
                .map(this::toDTO)// Esse this é uma função que transforma a lista de model de produto em uma lista de
                //DTO
                .toList();

    }

    //GET /produtos/{id}
    public ProdutoResponseDTO findById(String id){
        //Retorna o produto pelo id ou um erro
        ProdutosModel model = produtosRepository.findById(id)
                //Como existe uma chance do ID não existir, temos esse
                // filtro para o Java lançar um erro automático para o produto não encontrado
                .orElseThrow( () -> new RuntimeException("Produto não encontrado!"));
        return toDTO(model);
    }

    //POST /produtos
    public ProdutoResponseDTO save(ProdutoRequestDTO dto){
        ProdutosModel produtoSalvo = produtosRepository.save(toEntity(dto));
        return toDTO(produtoSalvo);
    }

    //DELETE /produtos/id
    public void delete(String id){
        this.findById(id);//Primeiro verifica se existe. Se não existir, já lançando o erro, reaproveitamento de código
        produtosRepository.deleteById(id);//Se passou pela linha 37 apagar normalmente
    }

    //PUT /produtos/{id}
    public ProdutoResponseDTO update(String id, ProdutoRequestDTO dto){
        ProdutosModel model = produtosRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Produto não encontrado!"));//Mesma coisa do delete, verifica se existe. Se não existir, temos um erro personalizado para isso.
        model.setDescricao(dto.descricao());
        model.setValorPontos(dto.valorPontos());
        ProdutosModel produtoAtualizado = produtosRepository.save(model);
        return toDTO(produtoAtualizado);
    }
}
