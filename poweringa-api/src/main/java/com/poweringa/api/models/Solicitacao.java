package com.poweringa.api.models;

import com.poweringa.api.enums.SolicitacaoStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "solicitacoes")
public class Solicitacao {
    @Id
    private String id;
    private String descricao;
    private SolicitacaoStatus status = SolicitacaoStatus.PENDENTE;
    //TO DO - Campo de idCategoria

    public Solicitacao(String descricao) {
        this.descricao = descricao;
    }
}
