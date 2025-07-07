package com.devsuperior.dscatalog.dto;

// A classe UserInsertDTO terá tudo que o UserDTO tem
public class UserInsertDTO extends UserDTO {

    private static final Long serialVersionUID = 1L;

    // Atributos

    private String password;

    // Construtor vazio

    UserInsertDTO() {
        super();
    }

    // Getter e Setter

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
