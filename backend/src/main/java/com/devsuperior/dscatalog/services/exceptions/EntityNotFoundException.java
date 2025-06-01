package com.devsuperior.dscatalog.services.exceptions;

public class EntityNotFoundException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    // Construtor que recebe uma mensagem
    public EntityNotFoundException(String msg) {
        // Vamos repassar a mensagem para a super classe RuntimeException
        super(msg);
    }

}
