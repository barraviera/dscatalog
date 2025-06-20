package com.devsuperior.dscatalog.services.exceptions;

public class DatabaseException extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    // Construtor que recebe uma mensagem
    public DatabaseException(String msg) {
        // Vamos repassar a mensagem para a super classe RuntimeException
        super(msg);
    }

}
