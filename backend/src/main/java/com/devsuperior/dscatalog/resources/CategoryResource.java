package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// Anotação pra indicar que esta classe será um controlador rest que responderá requisições
@RestController
// Indicar qual é a rota rest
@RequestMapping(value = "/categories")
public class CategoryResource {

    // Vamos injetar a dependencia do CategoryService
    @Autowired
    private CategoryService service;

    // O tipo de retorno deste metodo será um ResponseEntity que é um objeto do spring que vai encapsular uma resposta http
    // podemos indicar qual é o tipo de dado que estará no corpo desta resposta, que no caso iremos retornar uma lista de categoria List<CategoryDTO>
    // findAll é o nome do metodo
    // Vamos usar o GetMapping pra indicar que este metodo é um endpoint
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {

        // Vamos usar o service.findAll(); para chamar o metodo que busca todas as categorias
        // e guardamos em um List de Category
        List<CategoryDTO> list = service.findAll();

        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(list);
    }

    // Buscar categoria por id
    // Com esta anotação ficará /categories/id
    @GetMapping(value = "/{id}")
    // @PathVariable faz com que pegue a variavel informada na rota e coloque no parametro
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {

        // Vamos usar o service.findById(); para chamar o metodo que busca categorias por id
        CategoryDTO dto = service.findById(id);

        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(dto);
    }


}
