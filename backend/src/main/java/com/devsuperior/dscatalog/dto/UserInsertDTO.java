package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;

// A classe UserInsertDTO terá tudo que o UserDTO tem
// Vamos usar a annotation pra fazer a validação. Está anotation está em service>validation>UserInsertValid
// está anotação que irá processar por de baixo dos panos a verificação se o email que o usuario está inserindo
// ja existe no banco de dados
@UserInsertValid
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
