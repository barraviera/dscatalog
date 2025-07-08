package com.devsuperior.dscatalog.resources.exceptions;

import java.io.Serializable;

public class FieldMessage implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String fieldName;
    private String message;

    // Construtor sem argumentos

    public FieldMessage() {
    }

    // Construtor com argumentos

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    // Getter e Setter

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
