package com.tecdes.satestedesistemas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

import com.tecdes.satestedesistemas.dto.TarefaDTO;
import com.tecdes.satestedesistemas.model.Tarefa;
import com.tecdes.satestedesistemas.repository.TarefaRepository;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {
    @Mock
    public TarefaRepository repository;

    @InjectMocks 
    public TarefaService service;

    @Test
    void deveCriarTarefa(){
        // Arrange
        TarefaDTO tarefaEntrada = createTarefaDTO();
        Tarefa tarefa = mapEntity(tarefaEntrada);
        when(repository.save(any(Tarefa.class))).thenReturn(tarefa);

        // Act
        TarefaDTO tarefaRetornada = service.create(tarefaEntrada);

        // Assert
        assertEquals(tarefaEntrada, tarefaRetornada);
        verify(repository, times(1)).save(any(Tarefa.class));
    }

    @Test 
    void deveRemoverTarefa() {
        // Arrange
        Long id = 1L;

        // Act
        service.delete(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deveAtualizarDescricaoTarefa() {
        // Arrange
        Long id = 1L;
        Tarefa tarefaExistente = mapEntity(createTarefaDTO());
        TarefaDTO dtoAtualizado = new TarefaDTO(id, "tarefa1", null, "descricaoAtualizada", true);
        Tarefa tarefaAtualizada = mapEntity(dtoAtualizado);
        when(repository.findById(id)).thenReturn(Optional.of(tarefaExistente));
        when(repository.save(tarefaExistente)).thenReturn(tarefaAtualizada);
        // Act
        TarefaDTO resultado = service.update(id, dtoAtualizado);
        // Assert
        assertEquals("descricaoAtualizada", resultado.descricao());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(tarefaExistente);
    }

    private TarefaDTO createTarefaDTO() {
        return new TarefaDTO(1L, "tarefa1", null, "descricao", true);
    }

    private Tarefa mapEntity(TarefaDTO dto) {
        return Tarefa.builder().id(dto.id()).nome(dto.nome()).ativo(dto.ativo()).lista(dto.lista()).descricao(dto.descricao()).build();
    }
}
