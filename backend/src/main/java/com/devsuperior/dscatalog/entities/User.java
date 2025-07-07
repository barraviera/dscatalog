package com.devsuperior.dscatalog.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// A anotação @Entity trata esta classe como um mapeamento para uma tabela
@Entity
// A anotação @Table indica que esta classe irá representar uma tabela no banco de dados
@Table(name = "tb_user")
public class User implements Serializable {

    private static final Long serialVersionUID = 1L;

    // Atributos

    // Anotação pra informar qual atributo será o id
    @Id
    // Anotação pra deixar o id auto incrementado
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    // Anotação pra indicar que o relacionamento será de N:N muitos pra muitos
    // ou seja 1 user pode ter varias roles e 1 role pode ter varios users
    @ManyToMany
    // Anotação pra indicar a tabela intermediaria que chamaremos de tb_user_role
    @JoinTable(name = "tb_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    // Lista de permissoes que 1 usuario pode ter
    private Set<Role> roles = new HashSet<>();

    // Construtor vazio

    public User() {
    }

    // Construtor com argumentos

    public User(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // Getter e Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    // Equals e Hascode

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
