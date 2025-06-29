package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

// Aqui vamos fazer um teste de unidade usando o Mockito pra ser um teste isolado e mais rapido
// O service nao vai ter acesso ao banco de dados real na hora de fazer os testes
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    // Atributos

    // Veja que ao inves de usar o @autowired, vamos usar o @InjectMocks do Mockito
    @InjectMocks
    private ProductService service;

    // Se for teste de unidade que nao carrega o contexto usamos o @Mock, caso contrario, usamos o @MockBean
    @Mock
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page; // representa uma pagina de dados pra usarmos nos testes
    private Product product;

    // Metodo que é executando antes de cada teste
    @BeforeEach
    void setUp() throws Exception {
        // Vamos definir um id que existe pra testarmos
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;

        // Vamos criar um produto usando a classe Factory que criarmos
        product = Factory.createProduct();
        // e passamos este produto para a lista
        page = new PageImpl<>(List.of(product));

        // Configurar um comportamento simulado usando Mockito
        // doNothing para dizer, não faça nada quando deletar um ojeto com id que existe
        // Quando chamarmos o metodo deleteById com um id existente (existingId), esse metodo nao vai retornar nada (doNothing)
        Mockito.doNothing().when(repository).deleteById(existingId);

        // Simular quando chamar o existsById do ProductRepository e o id existe temos que ter um retorno true
        // ou seja o id existe
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        // E, quando chamar o metodo existsById pro id que nao existe, temos que ter o retorno false
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        // Simular o uso do metodo deleteById do ProductRepository para um id que é vinculado a outra tabela
        // ele tem que lançar a excessao DataIntegrityViolationException
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        // Quando chamar o metodo existsById para um id que é vinculado a outra tabela, ele existe
        // entao temos que retornar true
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);

        // Quando chamar o findAll do ProductRepository passando um objeto qualquer ArgumentMatchers.any() do tipo Pageable
        // essa chamado do findAll terá que retornar o page contendo produto
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        // Quando chamar o repository.save passando qualquer objeto deve-se retorno um product
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        // Quando chamar o findById com um id que existe, tem que retornar um Optional com um product dentro
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

        // Quando chamar o findById com um id inexistente, tem que retornar um Optional vazio
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
    }

    // Metodos

    // Do ponto de vista do service, quando passarmos um id que existe para ser deletado ele, deletará e nao retornará mais nada(void)
    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        // vamos testar se ele nao lançará nenhuma excessao, isso quer dizer que o id existe e foi deletado com sucesso
        Assertions.assertDoesNotThrow(() -> { // dentro da lambda

            // chamamos o metodo delete do ProductService e passamos um id que sabemos que existe
            service.delete(existingId);
        });
        // o verify vai verificar se alguma chamada foi feita, ou seja, ele vai verificar se o metodo deleteById do ProductRepository
        // foi chamado nessa ação que fizemos no teste acima
        Mockito.verify(repository).deleteById(existingId);
    }

    // Testar quando o id nao existe tem que lançar a excessao ResourceNotFoundException
    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {

            // chamamos o delete do ProductService e passamos um id que nao existe
            // quando ele se deparou com a linha if(!repository.existsById(id)) { ele rodou o Mock abaixo
            // Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
            // caindo na Assertions.assertThrows(ResourceNotFoundException.class, () -> { passando o teste como correto
            service.delete(nonExistingId);

        });
    }

    // Metodo para testar quando um produto está ligado com outra entidade e é deletado
    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

        Assertions.assertThrows(DatabaseException.class, () -> {

            service.delete(dependentId);

        });
    }

    // Ele deve retornar uma page quando chamar o metodo findAllPaged
    @Test
    public void findAllPageShouldReturnPage() {

        // Criamos uma pagina zero de tamanho 10
        Pageable pageable = PageRequest.of(0, 10);
        // chamamos o metodo findAllPaged do ProductService passando um pageable como parametro
        Page<ProductDTO> result = service.findAllPaged(pageable);
        // testar se nao é nulo o resultado
        Assertions.assertNotNull(result);
        // testar para ver se o findAll foi chamado dentro do findAllPaged do ProductService
        // o times é pra indicar quantas vezes ele deveria ter sido chamado, no caso de 1, nem precisaria colocar ele
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }


}
