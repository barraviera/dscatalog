package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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

    // O tipo de retorno deste metodo será um ResponseEntity que é um objeto do spring que vai encapsular uma resposta http
    // podemos indicar qual é o tipo de dado que estará no corpo desta resposta, que no caso iremos retornar uma lista de categoria List<ProductDTO>
    // findAll é o nome do metodo
    // Vamos usar o GetMapping pra indicar que este metodo é um endpoint
    // Para usar paginação vamos trocar o retorno de List<ProductDTO> para Page<ProductDTO>
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            // Usando o @RequestParam informamos que sao parametros opcionais
            // @RequestParam(value = "page", defaultValue = "0") Integer page,
            // @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            // @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            // @RequestParam(value = "orderBy", defaultValue = "name") String orderBy
            // Obs. ao inves de passarmos os parametros um por um igual fizemos acima, vamos usar o Pageable do spring
            // desta forma ele fará isso automaticamente. O que era o page continuará sendo o page, o que era linesPerPage será o size,
            // e direction e orderBy será o sort
            Pageable pageable
    ) {
        // O direction veio como string e vamos converter para o tipo Direction do spring
        // PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        // Vamos usar o service.findAll(); para chamar o metodo que busca todas as categorias
        // e guardamos em um List de Product
        // Vamos trocar o nome do metodo service de findAll para findAllPaged
        // e o tipo de retorno nao será mais um List como List<ProductDTO>, mas sim um Page como Page<ProductDTO>
        Page<ProductDTO> list = service.findAllPaged(pageable);

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
