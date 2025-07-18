package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// Estendemos a interface JpaRepository do spring, com isso ja temos vários metodos para acessar os dados no banco
// <Product, Long> precisamos informar a Entidade e o tipo do id dela
// Ao colocar a anotação @Repository indicamos que ele será um componente injetavel do spring
// e em CategoryService temos que colocar ele como @Autowired
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Metodo customizado que criamos, para que seja uma consulta paginada
    // com trecho do nome do produto sendo opcional, lista de categorias do produto opcional
    // numero da pagina desejada e quantidade de itens por pagina
    // retornará uma Page do tipo ProductProjection. Este ProductProjection nós
    // vamos criar para customizar esse retorno
    // nesta consulta só estamos preocupados em buscar o id o nome dos produtos
    // nesta consulta podemos usar a linguagem sql normal ou a JPQL da Jpa
    // a JPQL converte depois em SQL normal, entao se for uma consulta complexa usaremos SQL normal
    // se for uma consulta mais simples vamos usar JPQL do Jpa
    // nesta usaremos o SQL normal/raiz
    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM (
                    SELECT DISTINCT tb_product.id, tb_product.name
                    FROM tb_product
                    INNER JOIN tb_product_category ON tb_product_category.product_id = tb_product.id
                    WHERE (:categoryIds IS NULL OR tb_product_category.category_id IN :categoryIds)
                    AND (LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%')))                    
                    ) AS tb_result
                    """,
                    countQuery = """
                    SELECT COUNT(*) FROM (
                    SELECT DISTINCT tb_product.id, tb_product.name
                    FROM tb_product
                    INNER JOIN tb_product_category ON tb_product_category.product_id = tb_product.id
                    WHERE (:categoryIds IS NULL OR tb_product_category.category_id IN :categoryIds)
                    AND (LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%')))
                    ) AS tb_result                    
                    """)
    Page<ProductProjection> searchProducts(List<Long> categoryIds, String name, Pageable pageable);

    // Buscar os produtos com suas categorias
    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id IN :productIds")
    List<Product> searchProductsWithCategories(List<Long> productIds);

}
