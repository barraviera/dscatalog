package com.devsuperior.dscatalog.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// A anotação @Entity trata esta classe como um mapeamento para uma tabela
@Entity
// A anotação @Table indica que esta classe irá representar uma tabela no banco de dados
@Table(name = "tb_category")
public class Category implements Serializable {

    private static final Long serialVersionUID = 1L;

    // Atributos

    // Anotação pra informar qual atributo será o id
    @Id
    // Anotação pra deixar o id auto incrementado
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Armazenar na coluna createdAt sem especificar o timezone (padrao UTC)
    // ex. se aqui for 14:00h ele armazenará como 17:00h, depois quando buscarmos esse registro teremos que fazer essa conversao para -3
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt; // o tipo Instant armazena data e hora

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;

    // O mappedBy faz o mapeamento inverso do que está em Product @ManyToMany
    @ManyToMany(mappedBy = "categories")
    // Lista de produtos que uma categoria pode ter
    private Set<Product> products = new HashSet<>();

    // Construtor vazio

    public Category() {
    }

    // Construtor com argumentos

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Set<Product> getProducts() {
        return products;
    }


    // Sempre quando chamarmos o save do Jpa e for a primeira vez ele vai executar esse PrePersist e
    // salvará no banco o momento em que foi criado o registro
    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

    // Aqui sempre que formos atualizar ele vai atualizar o valor do updatedAt com o momento atual
    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    // Setter

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    // outro metodo de comparação mais garantido porem mais lento pra comparar se um objeto é igual a outro

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    // hashCode serve pra comparar se um objeto é igual a outro
    // ela é bem mais rapido que o equals, porem nao é tão garantido

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
