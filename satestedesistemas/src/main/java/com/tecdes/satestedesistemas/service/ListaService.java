package com.tecdes.satestedesistemas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tecdes.satestedesistemas.dto.ListaDTO;
import com.tecdes.satestedesistemas.model.Lista;
import com.tecdes.satestedesistemas.model.Tarefa;
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
        return mapDTO(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lista não encontrada com ID: " + id)));
    }

    public List<ListaDTO> findAll() {
        return repository.findAll().stream().map(l -> mapDTO(l)).toList();
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private Boolean verificarMinimoAtivo(ListaDTO listaDTO) {
        Integer quantidadeDeAtivasMinimas = 1;

        Integer qtdAtualAtivas = listaDTO.tarefas().stream().filter(e -> e.isAtivo() == true)
                .collect(Collectors.toList()).size();

        return quantidadeDeAtivasMinimas <= qtdAtualAtivas;
    }

    private Boolean tarefaRepetida(ListaDTO listaDTO) {
        List<Tarefa> tarefas = listaDTO.tarefas();

        for (int i = 0; i < tarefas.size(); i++) {
            Tarefa tarefaAtual = tarefas.get(i);
            Long id1 = tarefaAtual.getId();
            String descricao1 = tarefaAtual.getDescricao();

            for (int j = i + 1; j < tarefas.size(); j++) {
                Tarefa tarefaComparada = tarefas.get(j);
                Long id2 = tarefaComparada.getId();
                String descricao2 = tarefaComparada.getDescricao();

                if (id1.equals(id2) || descricao1.equals(descricao2)) {
                    return true; // achou duplicata, pode parar
                }
            }
        }

        return false; // nenhuma duplicata encontrada
    }

    @Transactional
    public ListaDTO update(Long id, ListaDTO dto) {

        if (!verificarMinimoAtivo(dto)) {
            delete(id);
            return null;
        }

        Lista lista = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lista não encontrada com ID: " + id));

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
                lista.getUsuario());
    }

    private Lista mapEntity(ListaDTO dto) {
        return new Lista(
                dto.id(),
                dto.nome(),
                dto.tarefas(),
                dto.usuario());
    }

}
