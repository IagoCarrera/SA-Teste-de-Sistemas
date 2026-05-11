package com.tecdes.satestedesistemas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecdes.satestedesistemas.model.Lista;

public record TarefaDTO(
    Long id,
    String nome,
    @JsonIgnore
    Lista lista,
    String descricao,
    boolean ativo
) {

}
