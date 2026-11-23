package com.juego_todo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.juego_todo.dto.favoritoDTO;
import com.juego_todo.dto.productoDTO;
import com.juego_todo.model.favorito;
import com.juego_todo.model.producto;
import com.juego_todo.model.usuario;
import com.juego_todo.repository.favoritoRepository;
import com.juego_todo.repository.productoRepository;
import com.juego_todo.repository.usuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class favoritoService {

    @Autowired
    private favoritoRepository favoritoRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private productoRepository productoRepository;

    public List<favoritoDTO> obtenerPorUsuario(Long idUsuario) {
        return favoritoRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public favoritoDTO agregarFavorito(favoritoDTO dto) {
        if (favoritoRepository.existsByUsuarioIdUsuarioAndProductoIdProducto(dto.getIdUsuario(), dto.getIdProducto())) {
            throw new RuntimeException("El producto ya está en favoritos");
        }

        usuario user = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("El usuario no existe"));
        producto prod = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new RuntimeException("El producto no existe"));

        favorito fav = new favorito();
        fav.setUsuario(user);
        fav.setProducto(prod);

        favorito guardado = favoritoRepository.save(fav);
        return convertirADTO(guardado);
    }

    public void eliminarFavorito(Long idFavorito) {
        if (!favoritoRepository.existsById(idFavorito)) {
            throw new RuntimeException("El favorito no existe");
        }
        favoritoRepository.deleteById(idFavorito);
    }

    public void eliminarPorUsuarioYProducto(Long idUsuario, Long idProducto) {
        favorito fav = favoritoRepository
                .findByUsuarioIdUsuarioAndProductoIdProducto(idUsuario, idProducto)
                .orElseThrow(() -> new RuntimeException("El favorito no existe"));
        favoritoRepository.delete(fav);
    }

    private favoritoDTO convertirADTO(favorito fav) {
        if (fav == null) return null;
        favoritoDTO dto = new favoritoDTO();

        dto.setIdFavorito(fav.getIdFavorito());
        dto.setFechaAgregado(fav.getFechaAgregado());

        if (fav.getUsuario() != null) {
            dto.setIdUsuario(fav.getUsuario().getIdUsuario());
        }

        if (fav.getProducto() != null) {
            dto.setIdProducto(fav.getProducto().getIdProducto());
            dto.setProducto(convertirProductoADTO(fav.getProducto()));
        }

        return dto;
    }

    private productoDTO convertirProductoADTO(producto prod) {
        productoDTO prodDto = new productoDTO();
        prodDto.setIdProducto(prod.getIdProducto());
        prodDto.setNombre(prod.getNombre());
        prodDto.setPrecio(prod.getPrecio());
        prodDto.setDescripcion(prod.getDescripcion());
        prodDto.setOferta(prod.getOferta());
        prodDto.setImagenUrl(prod.getImagenUrl());
        prodDto.setLinkTiendaOficial(prod.getLinkTiendaOficial());

        if (prod.getCategoria() != null) {
            prodDto.setIdCategoria(prod.getCategoria().getIdCategoria());
            prodDto.setNombreCategoria(prod.getCategoria().getNombreCategoria());
        }

        return prodDto;
    }
}