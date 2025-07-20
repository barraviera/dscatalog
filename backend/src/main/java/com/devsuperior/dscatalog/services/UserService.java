package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.*;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Essa anotação registrará esta classe como um componente que vai participar da injeção de dependência do spring
// Vamos implementar o UserDetailsService do spring security
@Service
public class UserService implements UserDetailsService {

    // Para fazer as buscas precisaremos da instacia do UserRepository
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    // Anotação que indica que as operações são somente leitura no banco. Isso ajuda o Spring a otimizar o desempenho e a evitar bloqueios desnecessários.
    // Usado em metodos que apenas leem dados findAll(), findById()...
    @Transactional(readOnly = true)
    // Metodo que busca todas as categorias e tem como retorno uma lista do tipo CategoryDTO
    public Page<UserDTO> findAllPaged(
            // PageRequest pageRequest
            // ao inves de usarmos PageRequest, vamos usar o Pageable, pois estamos
            // usando ele em UserResource no metodo findAll
            Pageable pageable
    ) {
        // esse .findAll() ja é um metodo de busca pronto do Jpa, é só nós utilizarmos ele vai aceitar paginacao tambem, nao precisamos criar um
        // metodo personalizado
        // vamos atribuir os resultados à uma lista de Category
        Page<User> list = repository.findAll(pageable);

        // Vamos usar o .stream para converter a lista em um stream que permite trabalhar com funções de alta ordem(lambdas)
        // no caso, vamos usar o .map que transforma cada elemento original da nossa lisa em uma outra coisa, aplicando uma função a cada elemento da lista
        // x -> new UserDTO(x) = pra cada elemento da lista convertemos ele em um UserDTO
        // .collect(Collectors.toList()); = agora vamos converter o stream novamente para uma lista
        // Como o Page ja é um stream do java 8, vamos remover list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList()); o strem e o collect
        return list.map(x -> new UserDTO(x));

    }

    // Busca categoria por id
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {

        // O objeto Optional apareceu no java 8 pra evitar trabalhar com valor nulo
        // e quem retorna este tipo Optional é o metodo do Jpa findById, que nunca retornará um objeto nulo, podendo ter ou não uma categoria dentro
        Optional<User> obj = repository.findById(id);
        // Obter o objeto que está dentro do Optional
        // Usando o orElseThrow, caso nao tenha um objeto User ele permite informarmos uma excessao
        // Criamos a nossa excessao EntityNotFoundException pra quando buscar um id que nao existe
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));
        // Retornamos o resultado como um UserDTO
        return new UserDTO(entity);
    }

    // Metodo para inserir categoria no banco de dados
    // Veja que criamos uma classe UserInsertDTO que terá, alem dos atributos herdados de UserDTO,
    // tambem o password, e que servirá somente para o insert
    @Transactional
    public UserDTO insert(UserInsertDTO dto) {

        // Criamos um objeto
        User entity = new User();

        // Setamos a senha que veio de UserInsertDTO
        // usamos o metodo passwordEncoder da classe AppConfig.java para criptografar a senha
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        // entity.setName(dto.getName());
        // Para nao ter que ficar fazendo setName, setPrice, etc aqui no insert e no update
        // vamos criar um metodo auxiliar copyDtoToEntity que faça isso
        copyDtoToEntity(entity, dto);

        // Ignorar qualquer role que tenha vindo do json
        entity.getRoles().clear();
        // e vamos definir somente a permissao de Operator para o usuario que está se cadastrando
        Role role = roleRepository.findByAuthority("ROLE_OPERATOR");
        entity.getRoles().add(role);

        // Agora vamos salvar o objeto no banco
        entity = repository.save(entity);

        // agora temos que retornar a entidade como forma de UserDTO
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {

        try {

            // Iremos buscar a categoria pelo id usando o getReferenceById do proprio spring
            // Importante: quando vamos atualizar, usamos o getReferenceById para buscar, pois ele nao toca no banco, ele instancia um objeto provisório
            // e só quando mandamos salvar é que ele efetivamento toca no banco usando recurso. Diferente de usar o findById, que consumirá recurso para buscar
            // a categoria e depois novamente para salvar a categoria atualizada
            User entity = repository.getReferenceById(id);

            // entity.setName(dto.getName());
            copyDtoToEntity(entity, dto);

            // Salvando a categoria atualizada no banco
            entity = repository.save(entity);

            // Retornamos a entidade convertida pra UserDTO
            return new UserDTO(entity);

        } catch (EntityNotFoundException e) { // A excessao EntityNotFoundException se da quando tentamos salvar o objeto retornado do getReferenceById mas que o id nao existia
            // Agora vamos lançar a nossa excessao personalizada
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    // Metodo para deletar categoria pelo id
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        // Se nao existir o id recebido por parametro...
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        // Mas se passou pela verificação acima, iremos no try tentar deletar a categoria pelo id recebido por parametro
        try {
            repository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            // Caso tentemos deletar um id que nao existe o erro será capturado pelo DataIntegrityViolationException
            // e lançaremos uma excessao personalizada DatabaseException
            // Explicando o que é Integridade Redefencial. Se temos vários produtos que são vinculados a uma categoria, e apagamos esta categoria
            // os produtos terão o id da categoria, mas ela nao vai existir, causando um problema de referencia no banco de dados
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    // Este metodo auxiliar receberá como parametro um objeto User
    // e um User DTO, ele vai copiar os dados do UserDTO para o User
    // Esse metodo será private e outras classes nao poderão acessa-lo
    private void copyDtoToEntity(User entity, UserDTO dto) {

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        // Primeiro vamos limpar a lista de roles
        entity.getRoles().clear();

        // vamos percorrer a lista de roles que chegou via paramatro UserDTO dto
        // vamos acessar essa lista usando o dto.getRoles() e percorrer a lista
        // chamando cada elemento de roleDTO
        // usamos a classe roleRepository pra usar o metodo getReferenceById do spring
        // este getReferenceById instancia um objeto provisorio sem tocar no banco
        for (RoleDTO roleDTO : dto.getRoles()) {

            // Role instanciada
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            // Agora vamos associar as roles localizadas, dentro da entidade de User
            entity.getRoles().add(role);
            // no final, chamando este metodo copyDtoToEntity, teremos um objeto User pronto
            // para ser salva ou atualizar quando precisar
        }
    }

    // Metodo obrigatorio por termos implementado a UserDetailsService
    // este metodo busca no banco o usuario pelo nome de usuario
    // se o usuario nao existir será lançado uma excessao chamada UsernameNotFoundException
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if (result.size() == 0) {
            throw new UsernameNotFoundException("Email not found");
        }

        User user = new User();
        user.setEmail(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

    @Transactional(readOnly = true)
    public UserDTO findMe() {

        User entity = authService.authenticated();
        return new UserDTO(entity);
    }
}
