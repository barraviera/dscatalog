package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

// Classe auxiliar para ajudar nossa vida
public class Factory {

    // Metodo que cria um product
    public static Product createProduct() {

        // Criamos um produto com informações qualquer pra usarmos nos testes
        Product product = new Product(
            1L,
            "Phone",
            "Good Phone",
            800.0,
            "https://img.com/img.png",
            Instant.parse("2020-10-20T03:00:00Z")
        );
        // vamos colocar uma categoria pra este produto
        product.getCategories().add(new Category(2L, "Eletronics"));

        return product;
    }

    public static ProductDTO createProductDTO() {

        // Vamos criar um objeto Product usando o metodo acima
        Product product = createProduct();

        // E vamos usar o construtor do ProductDTO que recebe um objeto Product e uma lista de categorias
        return new ProductDTO(product, product.getCategories());
    }

}
