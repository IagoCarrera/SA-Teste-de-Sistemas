package com.tecdes.satestedesistemas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    void criarUsuario(){
        UsuarioDTO usuarioEntrada = createUsuarioDTO();
        Usuario usuario = mapToEntity(usuarioEntrada);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        UsuarioDTO usuarioRetornado = usuarioService.create(usuarioEntrada);

        assertEquals(usuarioEntrada, usuarioRetornado);
    }

    @Test
    void deveAcessarSuaPropriaLista(){
        
        Map<String,Object> mapDoUsuario1 = createUsuarioDTOPorNomeEListaParaUsuario("João");
        Usuario usuario1 = (Usuario) mapDoUsuario1.get("Usuario");
        Lista lista1 = (Lista) mapDoUsuario1.get("Lista");
        UsuarioDTO usuario1DTO = (UsuarioDTO) mapDoUsuario1.get("UsuarioDTO");

        when(listaRepository.findById(lista1.getId())).thenReturn(Optional.of(lista1));
        
        ListaDTO lista = usuarioService.acessarLista(usuario1DTO, mapToListaDTO(lista1));
        
        assertEquals(mapToListaDTO(lista1), lista);
    }

    private UsuarioDTO createUsuarioDTO(){
        return new UsuarioDTO(1L, "Vinicius", null);
    }

    private UsuarioDTO createUsuarioDTOPorNome(String nome){
        return new UsuarioDTO(1L, nome, null);
    }
    private Map<String,Object> createUsuarioDTOPorNomeEListaParaUsuario(String nome){
        Usuario usuario = new Usuario(1L, nome, new ArrayList<>());
        Lista lista = createList(1L, usuario);
        usuario.getListas().add(lista);

        Map<String,Object> map = new HashMap<>();
        map.put("Usuario", usuario);
        map.put("Lista", lista);
        map.put("UsuarioDTO", mapToUsuarioDTO(usuario));

        return map;
    }

    private Usuario mapToEntity(UsuarioDTO usuarioDTO){
        return new Usuario(usuarioDTO.id(), usuarioDTO.nome(), usuarioDTO.listas());
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

    private Lista mapToLista(Lista lista){
        return Lista.builder().id(lista.getId()).nome(lista.getNome()).tarefas(lista.getTarefas()).usuario(lista.getUsuario()).build();
    }

}
