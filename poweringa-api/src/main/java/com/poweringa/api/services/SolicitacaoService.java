package com.poweringa.api.services;

import com.poweringa.api.enums.UserRole;
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
    private UserRepository userRepository;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    public List<Solicitacao> findAll(User usuarioLogado) {
        if (usuarioLogado.getCargo() == UserRole.GESTOR) {
            return solicitacaoRepository.findAll();
        }

        return usuarioLogado.getSolicitacoes();
    }

    public Solicitacao create(Solicitacao solicitacao, User usuarioLogado) {
        Solicitacao solicitacaoSalva = solicitacaoRepository.save(solicitacao);

        saveSolicitacaoToUser(solicitacaoSalva, usuarioLogado);

        return solicitacaoSalva;
    }

    public void saveSolicitacaoToUser(Solicitacao solicitacao, User usuarioLogado) {
        usuarioLogado.getSolicitacoes().add(solicitacao);

        userRepository.save(usuarioLogado);
    }
}
