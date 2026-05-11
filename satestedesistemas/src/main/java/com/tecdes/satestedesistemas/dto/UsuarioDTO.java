package com.tecdes.satestedesistemas.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecdes.satestedesistemas.model.Lista;

public record UsuarioDTO(
    Long id,
    String nome,
    @JsonIgnore
    List<Lista> listas
) {

}
