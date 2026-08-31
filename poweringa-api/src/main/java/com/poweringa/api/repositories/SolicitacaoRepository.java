package com.poweringa.api.repositories;

import com.poweringa.api.models.Solicitacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoRepository extends MongoRepository<Solicitacao, String> {
}
