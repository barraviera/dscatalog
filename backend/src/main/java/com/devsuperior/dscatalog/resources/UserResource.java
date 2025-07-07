package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// Anotação pra indicar que esta classe será um controlador rest que responderá requisições
@RestController
// Indicar qual é a rota rest
@RequestMapping(value = "/users")
public class UserResource {

    // Vamos injetar a dependencia do UserService
    @Autowired
    private UserService service;

    // O tipo de retorno deste metodo será um ResponseEntity que é um objeto do spring que vai encapsular uma resposta http
    // podemos indicar qual é o tipo de dado que estará no corpo desta resposta, que no caso iremos retornar uma lista de categoria List<UserDTO>
    // findAll é o nome do metodo
    // Vamos usar o GetMapping pra indicar que este metodo é um endpoint
    // Para usar paginação vamos trocar o retorno de List<UserDTO> para Page<UserDTO>
    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(
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
        // e guardamos em um List de User
        // Vamos trocar o nome do metodo service de findAll para findAllPaged
        // e o tipo de retorno nao será mais um List como List<UserDTO>, mas sim um Page como Page<UserDTO>
        Page<UserDTO> list = service.findAllPaged(pageable);

        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(list);
    }

    // Buscar categoria por id
    // Com esta anotação ficará /categories/id
    @GetMapping(value = "/{id}")
    // @PathVariable faz com que pegue a variavel informada na rota e coloque no parametro ex. http://localhost:8080/User/1
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {

        // Vamos usar o service.findById(); para chamar o metodo que busca categorias por id
        UserDTO dto = service.findById(id);
        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(dto);
    }

    // Metodo para inserir categoria
    // Receberemos como parametro um objeto do tipo UserDTO. É preciso colocar a anotação @RequestBody
    // A anotação @PostMapping é para quando for inserir
    // o metodo insert recebe um UserInsertDTO, mas retorna um UserDTO
    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {

        // No UserService teremos o metodo insert que recebe um UserDTO para fazer a inserção no banco
        UserDTO newDto = service.insert(dto);

        // Montamos o recurso
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();

        // Vamos retornar uma resposta
        // Iremos usar como resposta o created 201 passando a uri do recurso ex. http://localhost:8080/categories/4
        return ResponseEntity.created(uri).body(newDto);
    }

    // Metodo para atualizar categoria
    // A anotação @PutMapping é para quando for atualizar
    // Este metodo precisa receber o id da categoria a ser editada
    // e tambem o corpo da requisição @RequestBody UserDTO dto
    // Este metodo irá retornar um UserDTO como mostra em ResponseEntity<UserDTO>
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@Valid @PathVariable Long id, @RequestBody UserDTO dto) {

        // No UserService teremos o metodo update que recebe um id e um UserDTO para fazer a atualização no banco
        dto = service.update(id, dto);

        // Vamos retornar uma resposta
        return ResponseEntity.ok().body(dto);

    }

    // Metodo para deletar categoria
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable Long id) {

        // No UserService teremos o metodo update que recebe um id e um UserDTO para fazer a atualização no banco
        service.delete(id);

        // Vamos retornar a resposta sem corpo, pois no delete nao precisamos
        // será retornado um 204 -> retorno deu certo e o corpo será vazio
        return ResponseEntity.noContent().build();

    }


}
