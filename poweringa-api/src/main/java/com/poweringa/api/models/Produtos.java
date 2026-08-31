package com.poweringa.api.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor// Cria o construtor vazio(O Spring precisa disso daqui, para instanciar quando buscar os dados no banco de dados para devolvar para a api)
@AllArgsConstructor//Esse daqui é para criar o construtor com todos os atributos, para usarmos para criar o objeto no código
@Data//lombok para os getter e setters
@Document(collection = "produtos")
public class Produtos {
    @Id
    private String id;

    private String descricao;

    private Integer valorPontos;
}
