package com.tecdes.satestedesistemas.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecdes.satestedesistemas.dto.TarefaDTO;
import com.tecdes.satestedesistemas.model.Lista;
import com.tecdes.satestedesistemas.model.Tarefa;
import com.tecdes.satestedesistemas.repository.TarefaRepository;

import jakarta.persistence.EntityNotFoundException;
@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository repository;

    @InjectMocks
    private TarefaService service;

    // TODO: substitua Lista.values()[0] por um valor real do seu enum, ex: Lista.A_FAZER
    private static final Lista LISTA_PADRAO = new Lista(1L, "Lista Teste", List.of(), null);
    private static Tarefa tarefaEntity;
    private static TarefaDTO tarefaDTO;   


    @BeforeAll
    void setUp() {
        tarefaEntity = new Tarefa(1L, "Tarefa Teste", LISTA_PADRAO, "Descrição teste", true);
        tarefaDTO    = new TarefaDTO(1L, "Tarefa Teste", LISTA_PADRAO, "Descrição teste", true);
    }

    // ------------------------------------------------------------------ create
    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("deve salvar e retornar o DTO corretamente")
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
        @DisplayName("deve chamar repository.save exatamente uma vez")
        void deveChamarSaveUmaVez() {
            when(repository.save(any(Tarefa.class))).thenReturn(tarefaEntity);

            service.create(tarefaDTO);

            verify(repository, times(1)).save(any(Tarefa.class));
        }
    }

    // ---------------------------------------------------------------- findById
    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("deve retornar o DTO quando tarefa existir")
        void deveRetornarDTOQuandoTarefaExistir() {
            when(repository.findById(1L)).thenReturn(Optional.of(tarefaEntity));

            TarefaDTO resultado = service.findById(1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.id()).isEqualTo(1L);
            assertThat(resultado.nome()).isEqualTo("Tarefa Teste");
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando tarefa não existir")
        void deveLancarExcecaoQuandoTarefaNaoExistir() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(99L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    // ----------------------------------------------------------------- findAll
    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("deve retornar lista de DTOs quando houver tarefas")
        void deveRetornarListaDeDTOs() {
            Tarefa outra = new Tarefa(2L, "Outra Tarefa", LISTA_PADRAO, "Desc B", false);
            when(repository.findAll()).thenReturn(List.of(tarefaEntity, outra));

            List<TarefaDTO> resultado = service.findAll();

            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).id()).isEqualTo(1L);
            assertThat(resultado.get(1).id()).isEqualTo(2L);
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não houver tarefas")
        void deveRetornarListaVazia() {
            when(repository.findAll()).thenReturn(List.of());

            List<TarefaDTO> resultado = service.findAll();

            assertThat(resultado).isEmpty();
        }
    }

    // ------------------------------------------------------------------ delete
    @Nested
    @DisplayName("delete()")
    class Delete {

        @Test
        @DisplayName("deve chamar repository.deleteById com o id correto")
        void deveChamarDeleteById() {
            doNothing().when(repository).deleteById(1L);

            service.delete(1L);

            verify(repository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("não deve chamar nenhum outro método além de deleteById")
        void naoDeveChamarOutrosMetodos() {
            doNothing().when(repository).deleteById(1L);

            service.delete(1L);

            verify(repository, only()).deleteById(1L);
        }
    }

    // ------------------------------------------------------------------ update
    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("deve atualizar os campos e retornar o DTO atualizado")
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
        @DisplayName("deve lançar EntityNotFoundException quando tarefa não existir no update")
        void deveLancarExcecaoQuandoNaoEncontrarParaAtualizar() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.update(99L, tarefaDTO))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");

            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deve chamar save após encontrar a entidade")
        void deveChamarSaveAposEncontrarEntidade() {
            when(repository.findById(1L)).thenReturn(Optional.of(tarefaEntity));
            when(repository.save(any(Tarefa.class))).thenReturn(tarefaEntity);

            service.update(1L, tarefaDTO);

            verify(repository, times(1)).findById(1L);
            verify(repository, times(1)).save(any(Tarefa.class));
        }
    }
}