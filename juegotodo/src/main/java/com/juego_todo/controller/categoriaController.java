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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juego_todo.dto.categoriaDTO;
import com.juego_todo.service.categoriaService;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class categoriaController {

    @Autowired
    public categoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<categoriaDTO>> todasLasCategorias() {
        List<categoriaDTO> categorias = categoriaService.obtenerTodo();
        if (categorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<categoriaDTO> buscarPorId(@PathVariable Long id) {
        try {
            categoriaDTO categoria = categoriaService.buscarPorId(id);
            return new ResponseEntity<>(categoria, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<categoriaDTO> agregarCategoria(@RequestBody categoriaDTO dto) {
        try {
            categoriaDTO guardada = categoriaService.guardarCategoria(dto);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<categoriaDTO> actualizarCategoria(@PathVariable Long id, @RequestBody categoriaDTO dto) {
        try {
            categoriaDTO actualizada = categoriaService.actualizarCategoria(id, dto);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        try {
            categoriaService.eliminarCategoria(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}