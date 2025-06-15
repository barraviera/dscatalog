package com.devsuperior.dscatalog.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    // Construtor que recebe uma mensagem
    public ResourceNotFoundException(String msg) {
        // Vamos repassar a mensagem para a super classe RuntimeException
        super(msg);
    }

}
