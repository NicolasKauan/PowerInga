package com.poweringa.api.repositories;

import com.poweringa.api.models.Produtos;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutosRepository extends MongoRepository<Produtos, String> {
}
