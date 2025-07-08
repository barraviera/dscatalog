package com.devsuperior.dscatalog.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

// Teremos todos os atributos e metodos da classe StandardError
public class ValidationError extends StandardError {

    private List<FieldMessage> errors = new ArrayList<>();

    // Getter

    public List<FieldMessage> getErrors() {
        return errors;
    }

    // Metodos

    // Pra adicionar um novo erro na lista, vamos usar este metodo passando o campo e a mensagem
    public void addError(String fieldName, String message) {

        errors.add(new FieldMessage(fieldName, message));

    }
}
