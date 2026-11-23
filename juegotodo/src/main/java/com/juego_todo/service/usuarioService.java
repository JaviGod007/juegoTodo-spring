package com.juego_todo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.juego_todo.dto.authResponseDTO;
import com.juego_todo.dto.usuarioDTO;
import com.juego_todo.model.usuario;
import com.juego_todo.repository.usuarioRepository;
import com.juego_todo.security.JwtUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class usuarioService {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public List<usuarioDTO> obtenerTodo() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public usuarioDTO buscarPorId(Long id) {
        usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El usuario no existe"));
        return convertirADTO(user);
    }

    public authResponseDTO registrarUsuario(usuarioDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        usuario user = new usuario();
        user.setNombreUsuario(dto.getNombreUsuario());
        user.setCorreo(dto.getCorreo());
        user.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        user.setRol(dto.getRol() != null && !dto.getRol().isBlank() ? dto.getRol().toUpperCase() : "USER");

        usuario guardado = usuarioRepository.save(user);
        String token = jwtUtil.generarToken(guardado.getIdUsuario(), guardado.getCorreo(), guardado.getRol());

        return construirAuthResponse(token, guardado);
    }

    public authResponseDTO login(String correo, String contrasena) {
        usuario user = usuarioRepository.findByCorreo(correo)
                .filter(u -> passwordEncoder.matches(contrasena, u.getContrasena()))
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));

        String token = jwtUtil.generarToken(user.getIdUsuario(), user.getCorreo(), user.getRol());

        return construirAuthResponse(token, user);
    }

    public authResponseDTO renovarToken(String correo) {
        usuario user = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String nuevoToken = jwtUtil.generarToken(user.getIdUsuario(), user.getCorreo(), user.getRol());

        return construirAuthResponse(nuevoToken, user);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("El usuario no existe");
        }
        usuarioRepository.deleteById(id);
    }

    private authResponseDTO construirAuthResponse(String token, usuario user) {
        authResponseDTO response = new authResponseDTO();
        response.setToken(token);
        response.setIdUsuario(user.getIdUsuario());
        response.setNombreUsuario(user.getNombreUsuario());
        response.setCorreo(user.getCorreo());
        response.setRol(user.getRol());
        return response;
    }

    private usuarioDTO convertirADTO(usuario user) {
        if (user == null) return null;
        usuarioDTO dto = new usuarioDTO();

        dto.setIdUsuario(user.getIdUsuario());
        dto.setNombreUsuario(user.getNombreUsuario());
        dto.setCorreo(user.getCorreo());
        dto.setRol(user.getRol());

        return dto;
    }
}