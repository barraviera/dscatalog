package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

// Como vamos precisar carregar o contexto da aplicação, vamos add esta anotação
@SpringBootTest
// Para voltar o banco ao formato original após cada teste. Ex. se tinha 25 produtos e o teste deletou 1
// ficando 24, no proximo teste voltará a ter 25 produtos novamente
@Transactional
// Estes testes serão de integração, ou seja, o Service irá conversar de verdade com o Repository, por exemplo
// entao nao vamos mockar nada, mas sim declarar
public class ProductServiceITTests {

    // ATRIBUTOS ---

    // Veja aqui que vamos injetar e nao mockar os dados
    @Autowired
    private ProductService service;

    // Vamos injetar o Repository tambem
    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    // METODOS ---

    // Metodo que é executando antes de cada teste
    @BeforeEach
    void setUp() throws Exception {

        // Vamos definir valores pras variaveis
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25;
    }

    @Test
    // deveria deletar o recurso quando o id existe
    public void deleteShouldDeleteResourceWhenIdExists() {

        // Vamos chamar o metodo delete do ProductService
        service.delete(existingId);

        // Vamos verificar se apos o delete o countTotalProducts - 1 é igual a
        // quantidade total de registro no banco repository.count()
        Assertions.assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    // deveria lançar a excessao ThrowResourceNotFoundException quando o id nao existe
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {

            // chamamos o delete do ProductService e passamos um id que nao existe
            // quando ele se deparou com a linha if(!repository.existsById(id)) { ele rodou o Mock abaixo
            // Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
            // caindo na Assertions.assertThrows(ResourceNotFoundException.class, () -> { passando o teste como correto
            service.delete(nonExistingId);
        });
    }

    @Test
    // Ao chamar o findAllPaged do ProductService deveria retornar um Page na pagina zero com 10 produtos
    public void findAllPagedShouldReturnPageWhenPage0Size10() {

        // Criamos um PageRequest de pagina zero quantidade de itens 10
        // pois o retorno do metodo findAllPaged do ProductService tem de retorno
        // um Page<ProductDTO>
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        // Não pode estar vazio, tem que ter objeto
        Assertions.assertFalse(result.isEmpty());
        // Vamos testar se realmente é a pagina zero
        Assertions.assertEquals(0, result.getNumber());
        // testar se a pagina contem 10 objetos
        Assertions.assertEquals(10, result.getSize());
        // testar se o total de produtos que buscar é igual a 25 que definimos em countTotalProducts
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    // Ao chamar o findAllPaged do ProductService deveria retornar um pagina vazia quando a pagina nao existe
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {

        // Vamos buscar a pagina de numero 50 contendo 10 elementos
        // mas sabemos que nao existe, pois temos apenas 25 elementos, 3 paginas
        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        // Tem que ser verdadeiro se o resultado é vazio
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    // Ao chamar o findAllPaged do ProductService deveria retornar um pagina ordenada por nome
    public void findAllPagedShouldReturnSortedPageWhenSortedByName() {

        // Buscar uma pagina de numero zero com 10 elementos ordenada pelo name
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        // Testar se nao é vazio
        Assertions.assertFalse(result.isEmpty());
        // Testar se o nome do primeiro elemento da pagina result.getContent().get(0).getName()
        // é igual a Macbook Pro
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());

    }

}
