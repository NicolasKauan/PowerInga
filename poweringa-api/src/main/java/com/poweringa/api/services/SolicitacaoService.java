package com.poweringa.api.services;

import com.poweringa.api.dtos.ClassificarSolicitacaoRequestDTO;
import com.poweringa.api.dtos.SolicitacaoCreateDTO;
import com.poweringa.api.dtos.SolicitacaoResponseDTO;
import com.poweringa.api.enums.SolicitacaoStatus;
import com.poweringa.api.enums.UserRole;
import com.poweringa.api.exceptions.ResourceNotFound;
import com.poweringa.api.models.Solicitacao;
import com.poweringa.api.models.User;
import com.poweringa.api.repositories.SolicitacaoRepository;
import com.poweringa.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    public List<SolicitacaoResponseDTO> findAll(User usuarioLogado) {
        if (usuarioLogado.getCargo() == UserRole.GESTOR) {
            List<Solicitacao> solicitacoes = solicitacaoRepository.findAll();

             return solicitacoes.stream()
                     .map(solicitacao -> toDTO(solicitacao))
                     .toList();
        }

        return solicitacaoRepository.findByUsuario_Id(usuarioLogado.getId())
                .stream()
                .map(solicitacao -> toDTO(solicitacao))
                .toList();
    }

    public SolicitacaoResponseDTO findById(String id, User usuarioLogado) {
        ResourceNotFound exception = new ResourceNotFound("ERRO: Solicitação com esse id não foi encontrada!");

        if (usuarioLogado.getCargo() == UserRole.GESTOR) {
            return toDTO(solicitacaoRepository.findById(id)
                    .orElseThrow(() -> exception));
        }

        return toDTO(solicitacaoRepository.findByIdAndUsuario_Id(id, usuarioLogado.getId())
                .orElseThrow(() -> exception));
    }

    public SolicitacaoResponseDTO create(SolicitacaoCreateDTO dto, User usuarioLogado) {
        return toDTO(solicitacaoRepository.save(toEntity(dto, usuarioLogado)));
    }

    public SolicitacaoResponseDTO approve(String id) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("ERRO: Solicitação com esse id não foi encontrada!"));;

        solicitacao.setStatus(SolicitacaoStatus.APROVADO);

        return toDTO(solicitacaoRepository.save(solicitacao));
    }

    public SolicitacaoResponseDTO reject(String id) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("ERRO: Solicitação com esse id não foi encontrada!"));

        solicitacao.setStatus(SolicitacaoStatus.REPROVADO);

        return toDTO(solicitacaoRepository.save(solicitacao));
    }


    // comando para que localizar a solicitação e atribuir os pontos para o usuario.
    public SolicitacaoResponseDTO classificar(String id, ClassificarSolicitacaoRequestDTO dto) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id).orElseThrow(()
                -> new ResourceNotFound("ERRO: Solicitação com esse id não foi encontrada!"));
    // Acima localiza o código para partir do ID e, caso não encontre, lança uma exceção informando que não foi encontrado.
        solicitacao.setCategoria(dto.categoria());
        solicitacaoRepository.save(solicitacao);

        //Pega o usuario da solicitação verifica os pontos atuais e soma com os pontos da categoria
        User usuario = solicitacao.getUsuario();
        usuario.setPontos(usuario.getPontos() + dto.categoria().getPontos());

        return toDTO(solicitacaoRepository.save(solicitacao));

    }

    public Solicitacao toEntity(SolicitacaoCreateDTO dto, User usuarioLogado) {
        return new Solicitacao(dto.descricao(), usuarioLogado);
    }

    public SolicitacaoResponseDTO toDTO (Solicitacao solicitacao) {
        return new SolicitacaoResponseDTO(
                solicitacao.getId(),
                solicitacao.getDescricao(),
                solicitacao.getStatus(),
                solicitacao.getUsuario().getId(),
                solicitacao.getCategoria()
        );
    }
}
