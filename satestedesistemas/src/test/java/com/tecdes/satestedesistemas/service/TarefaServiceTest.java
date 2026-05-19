package com.tecdes.satestedesistemas.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecdes.satestedesistemas.dto.TarefaDTO;
import com.tecdes.satestedesistemas.model.Tarefa;
import com.tecdes.satestedesistemas.repository.TarefaRepository;

import com.tecdes.satestedesistemas.model.Lista;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {
    @Mock
    public TarefaRepository repository;

    @InjectMocks 
    public TarefaService service;

    private static final Lista LISTA_PADRAO = new Lista(1L, "Lista Teste", List.of(), null);
    private static Tarefa tarefaEntity;
    private static TarefaDTO tarefaDTO;   

    @BeforeAll
    void setUp() {
        tarefaEntity = new Tarefa(1L, "Tarefa Teste", LISTA_PADRAO, "Descrição teste", true);
        tarefaDTO    = new TarefaDTO(1L, "Tarefa Teste", LISTA_PADRAO, "Descrição teste", true);
    }

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

    @Test
    void deveChamarSaveAposEncontrarEntidade() {
        when(repository.findById(1L)).thenReturn(Optional.of(tarefaEntity));
        when(repository.save(any(Tarefa.class))).thenReturn(tarefaEntity);

        service.update(1L, tarefaDTO);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarParaAtualizar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, tarefaDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).save(any());
    }

    @Test
    void deveAtualizarCamposERetornarDTO() {
        TarefaDTO dtoAtualizado     = new TarefaDTO(1L, "Nome Novo", LISTA_PADRAO, "Desc Nova", false);
        Tarefa    entidadeAtualizada = new Tarefa(1L,  "Nome Novo", LISTA_PADRAO, "Desc Nova", false);

        when(repository.findById(1L)).thenReturn(Optional.of(tarefaEntity));
        when(repository.save(any(Tarefa.class))).thenReturn(entidadeAtualizada);

        TarefaDTO resultado = service.update(1L, dtoAtualizado);

        assertThat(resultado.nome()).isEqualTo("Nome Novo");
        assertThat(resultado.lista()).isEqualTo(LISTA_PADRAO);
        assertThat(resultado.descricao()).isEqualTo("Desc Nova");
        assertThat(resultado.ativo()).isFalse();
    }

    @Test
    void naoDeveChamarOutrosMetodos() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, only()).deleteById(1L);
    }

    @Test
    void deveSalvarERetornarDTO() {
        when(repository.save(any(Tarefa.class))).thenReturn(tarefaEntity);

        TarefaDTO resultado = service.create(tarefaDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(tarefaEntity.getId());
        assertThat(resultado.nome()).isEqualTo(tarefaEntity.getNome());
        assertThat(resultado.lista()).isEqualTo(tarefaEntity.getLista());
        assertThat(resultado.descricao()).isEqualTo(tarefaEntity.getDescricao());
        assertThat(resultado.ativo()).isEqualTo(tarefaEntity.isAtivo());

        verify(repository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveChamarSaveUmaVez() {
        when(repository.save(any(Tarefa.class))).thenReturn(tarefaEntity);

        service.create(tarefaDTO);

        verify(repository, times(1)).save(any(Tarefa.class));
    }


    @Test
    void deveRetornarDTOQuandoTarefaExistir() {
        when(repository.findById(1L)).thenReturn(Optional.of(tarefaEntity));

        TarefaDTO resultado = service.findById(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("Tarefa Teste");
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoExistir() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deveRetornarListaDeDTOs() {
        Tarefa outra = new Tarefa(2L, "Outra Tarefa", LISTA_PADRAO, "Desc B", false);
        when(repository.findAll()).thenReturn(List.of(tarefaEntity, outra));

        List<TarefaDTO> resultado = service.findAll();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(1).id()).isEqualTo(2L);
    }

    @Test
    void deveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(List.of());

        List<TarefaDTO> resultado = service.findAll();

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveChamarDeleteById() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
        

    private TarefaDTO createTarefaDTO() {
        return new TarefaDTO(1L, "tarefa1", null, "descricao", true);
    }

    private Tarefa mapEntity(TarefaDTO dto) {
        return Tarefa.builder().id(dto.id()).nome(dto.nome()).ativo(dto.ativo()).lista(dto.lista()).descricao(dto.descricao()).build();
    }
}
