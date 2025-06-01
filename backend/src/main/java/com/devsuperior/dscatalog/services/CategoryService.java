package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<CategoryDTO> findAll() {
        // esse .findAll() ja é um metodo de busca pronto do Jpa, é só nós utilizarmos
        // vamos atribuir os resultados à uma lista de Category
        List<Category> list = repository.findAll();

        // Vamos usar o .stream para converter a lista em um stream que permite trabalhar com funções de alta ordem(lambdas)
        // no caso, vamos usar o .map que transforma cada elemento original da nossa lisa em uma outra coisa, aplicando uma função a cada elemento da lista
        // x -> new CategoryDTO(x) = pra cada elemento da lista convertemos ele em um CategoryDTO
        // .collect(Collectors.toList()); = agora vamos converter o stream novamente para uma lista
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

    }

    // Busca categoria por id
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {

        // O objeto Optional apareceu no java 8 pra evitar trabalhar com valor nulo
        // e quem retorna este tipo Optional é o metodo do Jpa findById, que nunca retornará um objeto nulo, podendo ter ou não uma categoria dentro
        Optional<Category> obj = repository.findById(id);
        // Obter o objeto que está dentro do Optional
        Category entity = obj.get();
        // Retornamos o resultado como um CategoryDTO
        return new CategoryDTO(entity);
    }

}
