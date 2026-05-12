package com.tecdes.satestedesistemas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecdes.satestedesistemas.dto.UsuarioDTO;
import com.tecdes.satestedesistemas.model.Usuario;
import com.tecdes.satestedesistemas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void CriarUsuario(){
        UsuarioDTO usuarioEntrada = createUsuarioDTO();
        Usuario usuario = mapDTO(usuarioEntrada);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        UsuarioDTO usuarioRetornado = usuarioService.create(usuarioEntrada);

        assertEquals(usuarioEntrada, usuarioRetornado);
    }

    private UsuarioDTO createUsuarioDTO(){
        return new UsuarioDTO(1L, "Vinicius", null);
    }

    private Usuario mapDTO(UsuarioDTO usuarioDTO){
        return new Usuario(usuarioDTO.id(), usuarioDTO.nome(), usuarioDTO.listas());
    }

}
