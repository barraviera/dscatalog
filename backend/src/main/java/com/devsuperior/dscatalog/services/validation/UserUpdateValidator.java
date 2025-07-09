package com.devsuperior.dscatalog.services.validation;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// A classe UserUpdateValidator implementa o ConstraintValidator que é uma interface do Beans Validation
// O UserUpdateDTO é uma classe que vai recener a annotation UserUpdateValid
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private UserRepository repository;

    // Este objeto HttpServletRequest guarda os dados da requisição
    // a partir dele vamos conseguir pegar o id da usuario que está sendo atualizado
    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    // Este metodo isValid do ConstraintValidator está testando se o meu objeto UserDTO
    // vai ser válido ou nao, veja que ele retorna um bool true ou false
    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        // Dessa forma pegamos os atributos da url
        // conjunto de pares chave, valor Ex. id, 2
        @SuppressWarnings("unchecked") // para esconder o alerta em amarelo
        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        // pegamos somente a chave id que estava na uri
        long userId = Long.parseLong(uriVars.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        // Vamos usar o metodo findByEmail que criamos em UserRepository
        // Obs. se ele nao encontrar ele retorna um null
        // entao vamos usar um if pra testar
        User user = repository.findByEmail(dto.getEmail());

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
        // Vamos testar se o email ja existe no banco
        // se for diferente de null é porque encontrou um email
        // senao ele retorna o usuario de acordo com o email encontrado
        // && verificamos tambem se o id que veio na requisição for diferente da id do usuario retornado
        // iremos adicionar um erro pois nao queremos
        // Resumindo: iremos conseguir alterar os dados do usuario de id 2, maria, se passarmos o email dela maria@gmail.com
        // mas nao conseguiremos atualizar o usuario de id 2, maria, se passarmos o email de outro usuario por ex. alex@gmail.com
        if(user != null && userId != user.getId()) {

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
