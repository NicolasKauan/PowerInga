package com.poweringa.api.services;

import com.poweringa.api.models.Solicitacao;
import com.poweringa.api.models.User;
import com.poweringa.api.repositories.SolicitacaoRepository;
import com.poweringa.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitacaoService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

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
