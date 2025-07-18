package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // ATRIBUTOS ---

    // Vamos injetar a dependencia do CategoryService
    @Autowired
    private CategoryService service;

    // METODOS ---

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {

        List<CategoryDTO> list = service.findAll();

        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(list);
    }

    // Buscar categoria por id
    // Com esta anotação ficará /categories/id
    @GetMapping(value = "/{id}")
    // @PathVariable faz com que pegue a variavel informada na rota e coloque no parametro ex. http://localhost:8080/category/1
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {

        // Vamos usar o service.findById(); para chamar o metodo que busca categorias por id
        CategoryDTO dto = service.findById(id);
        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(dto);
    }

    // Em ResourceServiceConfig deixamos todas as requisições liberadas
    // Entao vamos colocar restrição direto neste controller no metodo insert
    // Veja que nos metodos findAll e findById deixamos liberados
    // O usuario precisa ter role de ADMIN ou de OPERATOR pra fazer essa requisição
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
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

    // Restrição de requisição por roles
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
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

    // Restrição de requisição por roles
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    // Metodo para deletar categoria
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> delete(@PathVariable Long id) {

        // No CategoryService teremos o metodo update que recebe um id e um CategoryDTO para fazer a atualização no banco
        service.delete(id);

        // Vamos retornar a resposta sem corpo, pois no delete nao precisamos
        // será retornado um 204 -> retorno deu certo e o corpo será vazio
        return ResponseEntity.noContent().build();

    }


}
