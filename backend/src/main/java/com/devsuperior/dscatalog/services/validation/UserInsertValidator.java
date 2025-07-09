package com.devsuperior.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

// import javax.validation.ConstraintValidator;
// import javax.validation.ConstraintValidatorContext;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

// A classe UserInsertValidator implementa o ConstraintValidator que é uma interface do Beans Validation
// O UserInsertDTO é uma classe que vai recener a annotation UserInsertValid
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    // Este metodo isValid do ConstraintValidator está testando se o meu objeto UserDTO
    // vai ser válido ou nao, veja que ele retorna um bool true ou false
    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        // Vamos usar o metodo findByEmail que criamos em UserRepository
        // Obs. se ele nao encontrar ele retorna um null
        // entao vamos usar um if pra testar
        User user = repository.findByEmail(dto.getEmail());

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
        // Vamos testar se o email ja existe no banco
        // se for diferente de null é porque encontrou um email
        if(user != null) {

            // entao se o email ja existe no banco iremos adicionar um erro na lista
            list.add(new FieldMessage("email", "Email já existente"));

        }

        // Estamos percorrendo a lista de FieldMessage pra adicionar na lista de erros do Beans Validation esses erros
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        // Testa se a lista está vazia
        // se  lista estiver vazia quer dizer que nao teve nenhum erro adicionado nela
        return list.isEmpty();
    }
}
