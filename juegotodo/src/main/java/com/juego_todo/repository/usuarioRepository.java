package com.juego_todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juego_todo.model.usuario;

public interface usuarioRepository extends JpaRepository<usuario, Long> {
    Optional<usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
