package com.juego_todo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juego_todo.dto.authResponseDTO;
import com.juego_todo.dto.usuarioDTO;
import com.juego_todo.service.usuarioService;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class usuarioController {

    @Autowired
    public usuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<usuarioDTO>> todosLosUsuarios() {
        List<usuarioDTO> usuarios = usuarioService.obtenerTodo();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<usuarioDTO> buscarPorId(@PathVariable Long id) {
        try {
            usuarioDTO usuario = usuarioService.buscarPorId(id);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<authResponseDTO> registrarUsuario(@RequestBody usuarioDTO dto) {
        try {
            authResponseDTO respuesta = usuarioService.registrarUsuario(dto);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<authResponseDTO> login(@RequestBody usuarioDTO credenciales) {
        try {
            authResponseDTO respuesta = usuarioService.login(credenciales.getCorreo(), credenciales.getContrasena());
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/renovar")
    public ResponseEntity<authResponseDTO> renovarToken(Authentication authentication) {
        try {
            String correo = authentication.getName();
            authResponseDTO respuesta = usuarioService.renovarToken(correo);
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}