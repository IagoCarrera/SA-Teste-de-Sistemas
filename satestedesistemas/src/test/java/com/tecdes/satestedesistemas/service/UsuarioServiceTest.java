package com.tecdes.satestedesistemas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    void deveCriarUsuario(){
        UsuarioDTO usuarioEntrada = createUsuarioDTO();
        Usuario usuario = mapEntity(usuarioEntrada);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO usuarioRetornado = usuarioService.create(usuarioEntrada);

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

    private UsuarioDTO createUsuarioDTO(){
        return new UsuarioDTO(1L, "Vinicius", null);
    }

    private Usuario mapEntity(UsuarioDTO usuarioDTO){
        return new Usuario(usuarioDTO.id(), usuarioDTO.nome(), usuarioDTO.listas());
    }

}
