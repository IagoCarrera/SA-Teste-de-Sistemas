package com.tecdes.satestedesistemas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tecdes.satestedesistemas.dto.ListaDTO;
import com.tecdes.satestedesistemas.model.Lista;
import com.tecdes.satestedesistemas.repository.ListaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListaService {

    private final ListaRepository repository;

    @Transactional
    public ListaDTO create(ListaDTO dto) {
        return mapDTO(repository.save(mapEntity(dto)));
    }

    public ListaDTO findById(Long id) {
        return mapDTO(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lista não encontrada com ID: " + id)));
    }

    public List<ListaDTO> findAll() {
        return repository.findAll().stream().map(l -> mapDTO(l)).toList();
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public ListaDTO update(Long id, ListaDTO dto) {
        Lista lista = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lista não encontrada com ID: " + id));
        lista.setNome(dto.nome());
        lista.setTarefas(dto.tarefas());
        lista.setUsuario(dto.usuario());
        return mapDTO(repository.save(lista));
    }

    private ListaDTO mapDTO(Lista lista) {
        return new ListaDTO(
            lista.getId(),
            lista.getNome(),
            lista.getTarefas(),
            lista.getUsuario()
        );
    }

    private Lista mapEntity(ListaDTO dto) {
        return new Lista(
            dto.id(),
            dto.nome(),
            dto.tarefas(),
            dto.usuario()
        );
    }

}
