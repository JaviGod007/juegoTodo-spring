package com.juego_todo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juego_todo.dto.favoritoDTO;
import com.juego_todo.service.favoritoService;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "*")
public class favoritoController {

    @Autowired
    public favoritoService favoritoService;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<favoritoDTO>> obtenerPorUsuario(@PathVariable Long idUsuario) {
        List<favoritoDTO> favoritos = favoritoService.obtenerPorUsuario(idUsuario);
        if (favoritos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(favoritos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<favoritoDTO> agregarFavorito(@RequestBody favoritoDTO dto) {
        try {
            favoritoDTO guardado = favoritoService.agregarFavorito(dto);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{idFavorito}")
    public ResponseEntity<Void> eliminarFavorito(@PathVariable Long idFavorito) {
        try {
            favoritoService.eliminarFavorito(idFavorito);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/usuario/{idUsuario}/producto/{idProducto}")
    public ResponseEntity<Void> eliminarPorUsuarioYProducto(
            @PathVariable Long idUsuario,
            @PathVariable Long idProducto) {
        try {
            favoritoService.eliminarPorUsuarioYProducto(idUsuario, idProducto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}