package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

// Nesta classe fizemos um teste de integração, pois carregamos por exemplo o ProductRepository, o Product, etc
// diferente do teste de unidade que não fazemos esse carregamento, mas sim usamos o Mockito igual no ProductService
@DataJpaTest
public class ProductRepositoryTests {

    // Atributos

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long countTotalProducts;

    // Metodos

    // Metodo que será executado antes de cada um dos testes abaixo
    @BeforeEach
    void setUp() throws Exception {

        // esta variavel com o id 1 será inicializada antes de cada teste
        existingId = 1L;
        // vamos dar um valor pra variavel countTotalProducts
        countTotalProducts = 25L;
    }

    @Test
    // deletar objeto quando o id existir
    public void deleteShouldDeleteObjectWhenIdExists() {

        // chamamos o metodo de deletar do ProductRepository
        repository.deleteById(existingId);

        // vamos verificar se o id ainda existe
        Optional<Product> result = repository.findById(existingId);

        // testamos se nao está presente um objeto dentro do result
        // Não é pra ter objeto dentro da variavel result pois nós efetuamos a exclusao pelo id
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    // testar o save quando o id do objeto é nulo
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {

        // Vamos chamar a classe fabrica que criamos e usar o metodo createProduct
        Product product = Factory.createProduct();
        // vamos setar um id nulo pra testar
        product.setId(null);
        // vamos salvar no banco com o id nulo
        product = repository.save(product);
        // Vamos testar se o id do produto salvo nao é nulo
        Assertions.assertNotNull(product.getId());
        // Criamos uma variavel countTotalProducts e demos o valor pra simular que a quantidade de produtos sao 25
        // agora vamos testar se o product.getId() é 26 ou seja countTotalProducts + 1
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

}
