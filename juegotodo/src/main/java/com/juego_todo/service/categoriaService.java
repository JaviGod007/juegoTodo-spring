package com.juego_todo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.juego_todo.dto.categoriaDTO;
import com.juego_todo.model.categoria;
import com.juego_todo.repository.categoriaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class categoriaService {

    @Autowired
    private categoriaRepository categoriaRepository;

    public List<categoriaDTO> obtenerTodo() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public categoriaDTO buscarPorId(Long id) {
        categoria cat = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoría no existe"));
        return convertirADTO(cat);
    }

    public categoriaDTO guardarCategoria(categoriaDTO dto) {
        categoria cat = new categoria();
        cat.setNombreCategoria(dto.getNombreCategoria());
        cat.setImagenUrl(dto.getImagenUrl());

        categoria guardada = categoriaRepository.save(cat);
        return convertirADTO(guardada);
    }

    public categoriaDTO actualizarCategoria(Long id, categoriaDTO dto) {
        categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoría no existe"));

        existente.setNombreCategoria(dto.getNombreCategoria());
        existente.setImagenUrl(dto.getImagenUrl());

        categoria actualizada = categoriaRepository.save(existente);
        return convertirADTO(actualizada);
    }

    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("La categoría no existe");
        }
        categoriaRepository.deleteById(id);
    }

    private categoriaDTO convertirADTO(categoria cat) {
        if (cat == null) return null;
        categoriaDTO dto = new categoriaDTO();

        dto.setIdCategoria(cat.getIdCategoria());
        dto.setNombreCategoria(cat.getNombreCategoria());
        dto.setImagenUrl(cat.getImagenUrl());

        return dto;
    }
}