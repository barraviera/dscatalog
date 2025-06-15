package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    // Metodo para inserir categoria
    // Receberemos como parametro um objeto do tipo CategoryDTO. É preciso colocar a anotação @RequestBody
    // A anotação @PostMapping é para quando for inserir
    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {

        // No CategoryService teremos o metodo insert que recebe um CategoryDTO para fazer a inserção no banco
        dto = service.insert(dto);

        // Montamos o recurso
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        // Vamos retornar uma resposta
        // Iremos usar como resposta o created 201 passando a uri do recurso ex. http://localhost:8080/categories/4
        return ResponseEntity.created(uri).body(dto);
    }

    // Metodo para atualizar categoria
    // A anotação @PutMapping é para quando for atualizar
    // Este metodo precisa receber o id da categoria a ser editada
    // e tambem o corpo da requisição @RequestBody CategoryDTO dto
    // Este metodo irá retornar um CategoryDTO como mostra em ResponseEntity<CategoryDTO>
    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {

        // No CategoryService teremos o metodo update que recebe um id e um CategoryDTO para fazer a atualização no banco
        dto = service.update(id, dto);

        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(dto);

    }


}
