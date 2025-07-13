package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.projections.ProductProjection;
import com.devsuperior.dscatalog.services.ProductService;
import jakarta.validation.Valid;
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

// Anotação pra indicar que esta classe será um controlador rest que responderá requisições
@RestController
// Indicar qual é a rota rest
@RequestMapping(value = "/products")
public class ProductResource {

    // Vamos injetar a dependencia do ProductService
    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "categoryId", defaultValue = "0") String categoryId,
            Pageable pageable
    ) {
        Page<ProductDTO> list = service.findAllPaged(name, categoryId, pageable);

        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(list);
    }

    // Buscar categoria por id
    // Com esta anotação ficará /categories/id
    @GetMapping(value = "/{id}")
    // @PathVariable faz com que pegue a variavel informada na rota e coloque no parametro ex. http://localhost:8080/Product/1
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {

        // Vamos usar o service.findById(); para chamar o metodo que busca categorias por id
        ProductDTO dto = service.findById(id);
        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(dto);
    }

    // Restrição de requisição por roles
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    // Metodo para inserir categoria
    // Receberemos como parametro um objeto do tipo ProductDTO. É preciso colocar a anotação @RequestBody
    // A anotação @PostMapping é para quando for inserir
    // Vamos usar o @Valid pra funcionar as validações que fizemos em ProductDTO
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {

        // No ProductService teremos o metodo insert que recebe um ProductDTO para fazer a inserção no banco
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
    // e tambem o corpo da requisição @RequestBody ProductDTO dto
    // Este metodo irá retornar um ProductDTO como mostra em ResponseEntity<ProductDTO>
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@Valid @PathVariable Long id, @RequestBody ProductDTO dto) {

        // No ProductService teremos o metodo update que recebe um id e um ProductDTO para fazer a atualização no banco
        dto = service.update(id, dto);

        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(dto);

    }

    // Restrição de requisição por roles
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    // Metodo para deletar categoria
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> delete(@PathVariable Long id) {

        // No ProductService teremos o metodo update que recebe um id e um ProductDTO para fazer a atualização no banco
        service.delete(id);

        // Vamos retornar a resposta sem corpo, pois no delete nao precisamos
        // será retornado um 204 -> retorno deu certo e o corpo será vazio
        return ResponseEntity.noContent().build();

    }


}
