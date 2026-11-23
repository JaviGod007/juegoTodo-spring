package com.juego_todo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.juego_todo.dto.productoDTO;
import com.juego_todo.model.categoria;
import com.juego_todo.model.producto;
import com.juego_todo.repository.categoriaRepository;
import com.juego_todo.repository.productoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class productoService {

    @Autowired
    private productoRepository productoRepository;

    @Autowired
    private categoriaRepository categoriaRepository;

    public List<productoDTO> obtenerTodo() {
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public productoDTO buscarPorId(Long id) {
        producto prod = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El producto no existe"));
        return convertirADTO(prod);
    }

    public List<productoDTO> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<productoDTO> buscarPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoriaIdCategoria(idCategoria).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<productoDTO> obtenerOfertas() {
        return productoRepository.findByOfertaTrue().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public productoDTO guardarProducto(productoDTO dto) {
        producto prod = new producto();
        prod.setNombre(dto.getNombre());
        prod.setPrecio(dto.getPrecio());
        prod.setDescripcion(dto.getDescripcion());
        prod.setOferta(dto.getOferta());
        prod.setImagenUrl(dto.getImagenUrl());
        prod.setLinkTiendaOficial(dto.getLinkTiendaOficial());

        if (dto.getIdCategoria() != null) {
            categoria cat = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("La categoría no existe"));
            prod.setCategoria(cat);
        }

        producto guardado = productoRepository.save(prod);
        return convertirADTO(guardado);
    }

    public productoDTO actualizarProducto(Long id, productoDTO dto) {
        producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El producto no existe"));

        existente.setNombre(dto.getNombre());
        existente.setPrecio(dto.getPrecio());
        existente.setDescripcion(dto.getDescripcion());
        existente.setOferta(dto.getOferta());
        existente.setImagenUrl(dto.getImagenUrl());
        existente.setLinkTiendaOficial(dto.getLinkTiendaOficial());

        if (dto.getIdCategoria() != null) {
            categoria cat = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("La categoría no existe"));
            existente.setCategoria(cat);
        }

        producto actualizado = productoRepository.save(existente);
        return convertirADTO(actualizado);
    }

    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("El producto no existe");
        }
        productoRepository.deleteById(id);
    }

    private productoDTO convertirADTO(producto prod) {
        if (prod == null) return null;
        productoDTO dto = new productoDTO();

        dto.setIdProducto(prod.getIdProducto());
        dto.setNombre(prod.getNombre());
        dto.setPrecio(prod.getPrecio());
        dto.setDescripcion(prod.getDescripcion());
        dto.setOferta(prod.getOferta());
        dto.setImagenUrl(prod.getImagenUrl());
        dto.setLinkTiendaOficial(prod.getLinkTiendaOficial());

        if (prod.getCategoria() != null) {
            dto.setIdCategoria(prod.getCategoria().getIdCategoria());
            dto.setNombreCategoria(prod.getCategoria().getNombreCategoria());
        }

        return dto;
    }
}