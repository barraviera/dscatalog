package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

// @ControllerAdvice anotação pra que esta classe consiga interceptar excessao do controller e trata-las
@ControllerAdvice
public class ResourceExceptionHandler {

    // Anotação pra que ele saiba qual o tipo de excessao ele deve interceptar
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request) {

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.NOT_FOUND.value()); // NOT_FOUND é o erro 404
        error.setError("Recurso não encontrado");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI()); // pega o caminho da requisição que deu erro

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
