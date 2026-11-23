package com.juego_todo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ManejadorErrores {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity manejarValidacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map campos = new HashMap();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> campos.put(err.getField(), err.getDefaultMessage()));

        Map body = new HashMap();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("mensaje", "Hay campos inválidos");
        body.put("ruta", request.getRequestURI());
        body.put("campos", campos);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity manejarJsonMalFormado(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400,
                "error", "Bad Request",
                "mensaje", "El JSON está mal formado",
                "ruta", request.getRequestURI()
        ));
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity manejarMediaType(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(Map.of(
                "status", 415,
                "error", "Unsupported Media Type",
                "mensaje", "Envía el body como JSON (Content-Type: application/json)",
                "ruta", request.getRequestURI()
        ));
    }
}