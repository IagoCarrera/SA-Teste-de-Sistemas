package com.tecdes.satestedesistemas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tecdes.satestedesistemas.dto.UsuarioDTO;
import com.tecdes.satestedesistemas.model.Usuario;
import com.tecdes.satestedesistemas.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    @Transactional
    public UsuarioDTO create(UsuarioDTO dto) {
        return mapDTO(repository.save(mapEntity(dto)));
    }

    public UsuarioDTO findById(Long id) {
        return mapDTO(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id)));
    }

    public List<UsuarioDTO> findAll() {
        return repository.findAll().stream().map(u -> mapDTO(u)).toList();
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        Usuario usuario = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
        usuario.setNome(dto.nome());
        usuario.setListas(dto.listas());
        return mapDTO(repository.save(usuario));
    }

    // teste
     
    private UsuarioDTO mapDTO(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getListas()
        );
    }

    private Usuario mapEntity(UsuarioDTO dto) {
        return new Usuario(
            dto.id(),
            dto.nome(),
            dto.listas()
        );
    }

}
