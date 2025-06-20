package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Essa anotação registrará esta classe como um componente que vai participar da injeção de dependência do spring
@Service
public class CategoryService {

    // Para fazer as buscas precisaremos da instacia do CategoryRepository
    @Autowired
    private CategoryRepository repository;

    // Anotação que indica que as operações são somente leitura no banco. Isso ajuda o Spring a otimizar o desempenho e a evitar bloqueios desnecessários.
    // Usado em metodos que apenas leem dados findAll(), findById()...
    @Transactional(readOnly = true)
    // Metodo que busca todas as categorias e tem como retorno uma lista do tipo CategoryDTO
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        // esse .findAll() ja é um metodo de busca pronto do Jpa, é só nós utilizarmos ele vai aceitar paginacao tambem, nao precisamos criar um
        // metodo personalizado
        // vamos atribuir os resultados à uma lista de Category
        Page<Category> list = repository.findAll(pageRequest);

        // Vamos usar o .stream para converter a lista em um stream que permite trabalhar com funções de alta ordem(lambdas)
        // no caso, vamos usar o .map que transforma cada elemento original da nossa lisa em uma outra coisa, aplicando uma função a cada elemento da lista
        // x -> new CategoryDTO(x) = pra cada elemento da lista convertemos ele em um CategoryDTO
        // .collect(Collectors.toList()); = agora vamos converter o stream novamente para uma lista
        // Como o Page ja é um stream do java 8, vamos remover list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList()); o strem e o collect
        return list.map(x -> new CategoryDTO(x));

    }

    // Busca categoria por id
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {

        // O objeto Optional apareceu no java 8 pra evitar trabalhar com valor nulo
        // e quem retorna este tipo Optional é o metodo do Jpa findById, que nunca retornará um objeto nulo, podendo ter ou não uma categoria dentro
        Optional<Category> obj = repository.findById(id);
        // Obter o objeto que está dentro do Optional
        // Usando o orElseThrow, caso nao tenha um objeto Category ele permite informarmos uma excessao
        // Criamos a nossa excessao EntityNotFoundException pra quando buscar um id que nao existe
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));
        // Retornamos o resultado como um CategoryDTO
        return new CategoryDTO(entity);
    }

    // Metodo para inserir categoria no banco de dados
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {

        // Criamos um objeto
        Category entity = new Category();
        // Vamos converter de dto para Category
        entity.setName(dto.getName());
        // Agora vamos salvar o objeto no banco
        entity = repository.save(entity);

        // agora temos que retornar a entidade como forma de CategoryDTO
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {

        try {

            // Iremos buscar a categoria pelo id usando o getReferenceById do proprio spring
            // Importante: quando vamos atualizar, usamos o getReferenceById para buscar, pois ele nao toca no banco, ele instancia um objeto provisório
            // e só quando mandamos salvar é que ele efetivamento toca no banco usando recurso. Diferente de usar o findById, que consumirá recurso para buscar
            // a categoria e depois novamente para salvar a categoria atualizada
            Category entity = repository.getReferenceById(id);
            // Set um novo nome que veio por parametro pra categoria buscada
            entity.setName(dto.getName());
            // Salvando a categoria atualizada no banco
            entity = repository.save(entity);

            // Retornamos a entidade convertida pra CategoryDTO
            return new CategoryDTO(entity);

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
}
