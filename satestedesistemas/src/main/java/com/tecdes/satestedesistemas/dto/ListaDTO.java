package com.tecdes.satestedesistemas.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecdes.satestedesistemas.model.Tarefa;
import com.tecdes.satestedesistemas.model.Usuario;

import lombok.Builder;

@Builder
public record ListaDTO(
    Long id,
    String nome,
    @JsonIgnore
    List<Tarefa> tarefas,
    Usuario usuario
) {}
