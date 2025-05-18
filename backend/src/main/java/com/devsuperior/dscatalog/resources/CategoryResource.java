package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// Anotação pra indicar que esta classe será um controlador rest que responderá requisições
@RestController
// Indicar qual é a rota rest
@RequestMapping(value = "/categories")
public class CategoryResource {

    // O tipo de retorno deste metodo será um ResponseEntity que é um objeto do spring que vai encapsular uma resposta http
    // podemos indicar qual é o tipo de dado que estará no corpo desta resposta, que no caso iremos retornar uma lista de categoria List<Category>
    // findAll é o nome do metodo
    // Vamos usar o GetMapping pra indicar que este metodo é um endpoint
    @GetMapping
    public ResponseEntity<List<Category>> findAll() {

        // No java a List é uma interface, e nao podemos instanciar uma interface
        // nós temos que instanciar uma classe que implemente a interface List, que no caso usamos a ArrayList
        List<Category> list = new ArrayList<>();
        // add elementos na lista
        // A letra L no id é porque o tipo do id é um Long
        list.add(new Category(1L, "Books"));
        list.add(new Category(2L, "Electonics"));

        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(list);
    }

}
