package com.devsuperior.dscatalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {

    // ATRIBUTOS ---

    @NotBlank(message = "Campo obrigatório")
    @Email(message = "Email inválido")
    private String email;

    // CONSTRUTOR ---

    public EmailDTO() {
    }

    public EmailDTO(String email) {
        this.email = email;
    }

    // GETTERS E SETTERS

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}