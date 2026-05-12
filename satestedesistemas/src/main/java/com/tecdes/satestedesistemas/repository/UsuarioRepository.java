package com.tecdes.satestedesistemas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecdes.satestedesistemas.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
