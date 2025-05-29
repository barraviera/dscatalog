package com.devsuperior.dscatalog.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

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
