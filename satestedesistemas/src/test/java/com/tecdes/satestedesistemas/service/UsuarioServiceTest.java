package com.tecdes.satestedesistemas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecdes.satestedesistemas.dto.ListaDTO;
import com.tecdes.satestedesistemas.dto.UsuarioDTO;
import com.tecdes.satestedesistemas.model.Lista;
import com.tecdes.satestedesistemas.model.Usuario;
import com.tecdes.satestedesistemas.repository.ListaRepository;
import com.tecdes.satestedesistemas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ListaRepository listaRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveAcessarSuaPropriaLista(){  
        // Arrange
        Usuario usuario1 = createUsuario(1L, "João");
        Lista lista1 = createList(1L, usuario1);
        when(listaRepository.findById(lista1.getId())).thenReturn(Optional.of(lista1));
        
        // Act
        ListaDTO lista = usuarioService.acessarLista(mapToUsuarioDTO(usuario1), mapToListaDTO(lista1));
        
        // Assert
        assertEquals(mapToListaDTO(lista1), lista);
    }

    @Test
    void naoDeveAcessarListaDeOutros(){
        // Arrange
        Usuario usuario1 = createUsuario(1L, "João");
        Lista lista1 = createList(1L, usuario1);

        // Act
        Usuario usuario2 = createUsuario(2L, "Arthur");
        ListaDTO lista = usuarioService.acessarLista(mapToUsuarioDTO(usuario2), mapToListaDTO(lista1));
       
        // Assert
        verify(listaRepository, never()).findById(anyLong());
        assertNull(lista);
    }

    @Test
    void deveCriarUsuario(){
        // Arrange
        UsuarioDTO usuarioEntrada = mapToUsuarioDTO(createUsuario(1L, "usuario"));
        Usuario usuario = mapEntity(usuarioEntrada);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        UsuarioDTO usuarioRetornado = usuarioService.create(usuarioEntrada);

        // Assert
        assertEquals(usuarioEntrada, usuarioRetornado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test 
    void deveRemoverUsuario() {
        // Arrange
        Long id = 1L;

        // Act
        usuarioService.delete(id);

        // Assert
        verify(usuarioRepository, times(1)).deleteById(id);
    }

    private Usuario createUsuario(Long id, String nome){
        return new Usuario(id, nome, new ArrayList<>());
    }

    private UsuarioDTO mapToUsuarioDTO(Usuario usuario){
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getListas());
    }

    private Lista createList(Long id, Usuario usuario){
        return Lista.builder().id(id).nome("Teste").tarefas(null).usuario(usuario).build();
    }

    private ListaDTO mapToListaDTO(Lista lista){
        return new ListaDTO(lista.getId(), lista.getNome(), lista.getTarefas(), lista.getUsuario());
    }
    
    private Usuario mapEntity(UsuarioDTO usuarioDTO){
        return new Usuario(usuarioDTO.id(), usuarioDTO.nome(), usuarioDTO.listas());
    }

}
