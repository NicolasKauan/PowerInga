package com.poweringa.api.repositories;

import com.poweringa.api.models.Solicitacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends MongoRepository<Solicitacao, String> {
    Optional<Solicitacao> findByIdAndUsuario_Id(String id, String idUser);
}
