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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.juego_todo.dto.productoDTO;
import com.juego_todo.service.productoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class productoController {

    @Autowired
    public productoService productoService;

    @GetMapping
    public ResponseEntity<List<productoDTO>> todosLosProductos() {
        List<productoDTO> productos = productoService.obtenerTodo();
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<productoDTO> buscarPorId(@PathVariable Long id) {
        try {
            productoDTO producto = productoService.buscarPorId(id);
            return new ResponseEntity<>(producto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<productoDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<productoDTO> productos = productoService.buscarPorNombre(nombre);
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<productoDTO>> buscarPorCategoria(@PathVariable Long idCategoria) {
        List<productoDTO> productos = productoService.buscarPorCategoria(idCategoria);
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/ofertas")
    public ResponseEntity<List<productoDTO>> obtenerOfertas() {
        List<productoDTO> ofertas = productoService.obtenerOfertas();
        if (ofertas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ofertas, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<productoDTO> agregarProducto(@RequestBody productoDTO dto) {
        try {
            productoDTO guardado = productoService.guardarProducto(dto);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<productoDTO> actualizarProducto(@PathVariable Long id, @RequestBody productoDTO dto) {
        try {
            productoDTO actualizado = productoService.actualizarProducto(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}