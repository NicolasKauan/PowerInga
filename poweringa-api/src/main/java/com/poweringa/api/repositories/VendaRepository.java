package com.poweringa.api.repositories;

import com.poweringa.api.models.Venda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendaRepository extends MongoRepository<Venda, String> {
    List<Venda> findByUsuario_Id(String idUser);

    Optional<Venda> findByIdAndUsuario_Id(String id, String idUser);
}
