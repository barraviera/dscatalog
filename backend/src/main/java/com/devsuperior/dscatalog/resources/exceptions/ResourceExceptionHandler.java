package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
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
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value()); // NOT_FOUND é o erro 404
        error.setError("Recurso não encontrado");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI()); // pega o caminho da requisição que deu erro

        return ResponseEntity.status(status).body(error);
    }

    // Caso aconteça uma excessao do tipo DatabaseException que chegue no controlador, cairá neste metodo
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value()); // BAD_REQUEST é o erro 400
        error.setError("Database exception");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI()); // pega o caminho da requisição que deu erro

        return ResponseEntity.status(status).body(error);
    }

}
