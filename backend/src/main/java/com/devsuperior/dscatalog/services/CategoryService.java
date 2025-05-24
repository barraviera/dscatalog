package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Essa anotação registrará esta classe como um componente que vai participar da injeção de dependência do spring
@Service
public class CategoryService {

    // Para fazer as buscas precisaremos da instacia do CategoryRepository
    @Autowired
    private CategoryRepository repository;

    // Metodo que busca todas as categorias
    public List<Category> findAll() {
        // esse .findAll() ja é um metodo de busca pronto do Jpa, é nós utilizarmos
        return repository.findAll();
    }

}
