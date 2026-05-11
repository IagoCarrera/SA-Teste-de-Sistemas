package com.tecdes.satestedesistemas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecdes.satestedesistemas.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

}
