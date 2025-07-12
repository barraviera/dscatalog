package com.devsuperior.dscatalog.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Objects;

// @SuppressWarnings("serial")
// A anotação @Entity trata esta classe como um mapeamento para uma tabela
@Entity
// A anotação @Table indica que esta classe irá representar uma tabela no banco de dados
@Table(name = "tb_role")
// Vamos implementar a interface GrantedAuthority do spring security
public class Role implements GrantedAuthority {

    // O serializable é um recurso antigo do java que, quando a classe era um serializable,
    // ela podia ser convertida em bytes, permitindo gravar o objeto em arquivo ou transferir em uma rede, etc
    // o GrantedAuthority é uma classe serializable e ele pede um número de seria pra classe
    // Ex. private static final long serialVersionUID = 1L;
    // mas nao iremos utiliza-lo, caso apareça um erro em amarelo, coloque @SuppressWarnings("serial")

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

    // Já tinhamos um metodo com o nome getAuthority
    // mas ao implementar a classe GrantedAuthority ela iria pedir obrigatoriamente pra
    // implementar este metodo, entao iremos colocar um @Override pra dizer que estamos sobrescrevendo
    // e usando o metodo de GrantedAuthority
    @Override
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
