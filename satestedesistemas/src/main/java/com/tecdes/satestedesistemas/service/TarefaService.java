package com.tecdes.satestedesistemas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tecdes.satestedesistemas.dto.TarefaDTO;
import com.tecdes.satestedesistemas.model.Tarefa;
import com.tecdes.satestedesistemas.repository.TarefaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository repository;

    @Transactional
    public TarefaDTO create(TarefaDTO dto) {
        return mapDTO(repository.save(mapEntity(dto)));
    }

    public TarefaDTO findById(Long id) {
        return mapDTO(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com ID: " + id)));
    }

    public List<TarefaDTO> findAll() {
        return repository.findAll().stream().map(t -> mapDTO(t)).toList();
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public TarefaDTO update(Long id, TarefaDTO dto) {
        Tarefa tarefa = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com ID: " + id));
        tarefa.setNome(dto.nome());
        tarefa.setLista(dto.lista());
        tarefa.setDescricao(dto.descricao());
        tarefa.setAtivo(dto.ativo());
        return mapDTO(repository.save(tarefa));
    }

    private TarefaDTO mapDTO(Tarefa tarefa) {
        return new TarefaDTO(
            tarefa.getId(),
            tarefa.getNome(),
            tarefa.getLista(),
            tarefa.getDescricao(),
            tarefa.isAtivo()
        );
    }

    private Tarefa mapEntity(TarefaDTO dto) {
        return new Tarefa(
            dto.id(),
            dto.nome(),
            dto.lista(),
            dto.descricao(),
            dto.ativo()
        );
    }

}
