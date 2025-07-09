package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserUpdateValid;

// A classe UserUpdateDTO terá tudo que o UserDTO tem
// Vamos usar a annotation pra fazer a validação. Está anotation está em service>validation>UserUpdateValid
// está anotação que irá processar por de baixo dos panos a verificação se o email que o usuario está inserindo
// ja existe no banco de dados
@UserUpdateValid
public class UserUpdateDTO extends UserDTO {

    private static final Long serialVersionUID = 1L;

}
