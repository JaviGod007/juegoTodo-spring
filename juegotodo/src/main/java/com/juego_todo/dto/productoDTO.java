package com.juego_todo.dto;

import lombok.Data;

@Data
public class productoDTO {
    
    private Long idProducto;
    private Long idCategoria;
    private String nombreCategoria;
    private String nombre;
    private Double precio;
    private String descripcion;
    private Boolean oferta;
    private String imagenUrl;
    private String linkTiendaOficial;
}
