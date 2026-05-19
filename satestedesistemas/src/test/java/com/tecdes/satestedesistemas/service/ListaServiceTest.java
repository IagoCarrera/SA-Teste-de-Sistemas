package com.tecdes.satestedesistemas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecdes.satestedesistemas.dto.ListaDTO;
import com.tecdes.satestedesistemas.model.Lista;
import com.tecdes.satestedesistemas.model.Tarefa;
import com.tecdes.satestedesistemas.model.Usuario;
import com.tecdes.satestedesistemas.repository.ListaRepository;

@ExtendWith(MockitoExtension.class)
public class ListaServiceTest {

    @Mock
    private ListaRepository listaRepository;

    @InjectMocks
    private ListaService listaService;

    @Test
    void deveCriarLista() {
        // Arrange
        ListaDTO listaDTO = criarListaTeste();
        Lista listaEntrada = mapEntity(listaDTO);
        when(listaRepository.save(any(Lista.class))).thenReturn(listaEntrada);

        // Act
        ListaDTO dto = listaService.create(listaDTO);

        // Assert
        assertEquals(listaDTO, dto);
    }

    @Test
    void deveTerPeloMenosUmaAtiva() {
        // Arrange
        ListaDTO listaDTO = criarListaTeste();
        listaDTO.tarefas().forEach((tarefa) -> {
            tarefa.setAtivo(false);
        });

        // Act
        listaService.update(listaDTO.id(), listaDTO);

        // Assert
        verify(listaRepository).deleteById(listaDTO.id());
        verify(listaRepository, never()).save(any(Lista.class));
    }

    @Test
    void deveNaoTerRepetida() {
        // Arrange
        ListaDTO listaDTO = criarListaTeste();
        Tarefa tarefaRepetida = listaDTO.tarefas().get(0);
        listaDTO.tarefas().add(tarefaRepetida);

        // Act
        ListaDTO listaNull = listaService.update(listaDTO.id(), listaDTO);

        // Assert
        verify(listaRepository, never()).save(any(Lista.class));
        assertNull(listaNull);
    }

    @Test
    void deveCriarTarefa(){
        // Arrange
        ListaDTO tarefaEntrada = criarListaTeste();
        Lista tarefa = mapEntity(tarefaEntrada);
        when(listaRepository.save(any(Lista.class))).thenReturn(tarefa);

        // Act
        ListaDTO tarefaRetornada = listaService.create(tarefaEntrada);

        // Assert
        assertEquals(tarefaEntrada, tarefaRetornada);
        verify(listaRepository, times(1)).save(any(Lista.class));
    }

    @Test 
    void deveRemoverTarefa() {
        // Arrange
        Long id = 1L;

        // Act
        listaService.delete(id);

        // Assert
        verify(listaRepository, times(1)).deleteById(id);
    }

    private Lista mapEntity(ListaDTO listaDTO) {
        return Lista.builder()
                .id(listaDTO.id())
                .tarefas(listaDTO.tarefas())
                .nome(listaDTO.nome())
                .usuario(listaDTO.usuario())
                .build();
    }

    private ListaDTO criarListaTeste() {
        Usuario usuario = new Usuario(1l, "Cara dos teses", (List<Lista>) new ArrayList<Lista>());
        Tarefa tarefa1 = new Tarefa(1l, "Tarefa 01", null, "Descricação tarefa 01", true);
        Tarefa tarefa2 = new Tarefa(2l, "Tarefa 02", null, "Descricação tarefa 02", false);
        Tarefa tarefa3 = new Tarefa(3l, "Tarefa 03", null, "Descricação tarefa 03", true);

        List<Tarefa> tarefas = new ArrayList<>(List.of(tarefa1, tarefa2, tarefa3));

        ListaDTO lista = ListaDTO.builder()
                .id(1l)
                .nome("Lista de tarefas 1")
                .usuario(usuario)
                .tarefas(tarefas)
                .build();

        return lista;
    }
}
