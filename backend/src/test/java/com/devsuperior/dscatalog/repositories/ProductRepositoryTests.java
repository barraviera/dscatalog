package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    // Atributos

    @Autowired
    private ProductRepository repository;

    private long existingId;

    // Metodos

    // Metodo que será executado antes de cada um dos testes abaixo
    @BeforeEach
    void setUp() throws Exception {

        // esta variavel com o id 1 será inicializada antes de cada teste
        existingId = 1L;
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

}
