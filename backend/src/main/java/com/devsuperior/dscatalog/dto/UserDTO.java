package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    // Atributos

    private Long id;
    // O firstName nao pode ser vazio
    // O NotBlank verifica se inseriram espaços em branco e nenhum texto
    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;
    @Email(message = "Inserir um e-mail válido")
    private String email;

    Set<RoleDTO> roles = new HashSet<>();

    // Construtor sem argumentos

    public UserDTO() {
    }

    // Construtor com argumentos

    public UserDTO(Long id, String firstName, String lastName, String email) {
        // this é o atributo
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Construtor com entidade

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        // pegamos a lista de roles que está vindo junto da entity
        // devido a linha @ManyToMany(fetch = FetchType.EAGER) do User.java que carregamos as roles junto com o usuario
        // percorremos cada um dos roles, instanciamos usando new RoleDTO(role)
        // e inserimos na lista Set<RoleDTO> roles = new HashSet<>();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
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

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
