package com.devsuperior.dscatalog.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

// A anotação @Entity trata esta classe como um mapeamento para uma tabela
@Entity
// A anotação @Table indica que esta classe irá representar uma tabela no banco de dados
@Table(name = "tb_role")
public class Role implements Serializable {

    private static final Long serialVersionUID = 1L;

    // Atributos

    // Anotação pra informar qual atributo será o id
    @Id
    // Anotação pra deixar o id auto incrementado
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    // Construtor vazio

    public Role() {
    }

    // Construtor com argumentos

    public Role(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    // Getter e Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    // Equals e Hashcode

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
