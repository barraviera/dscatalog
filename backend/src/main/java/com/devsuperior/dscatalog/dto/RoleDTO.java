package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;

import java.io.Serializable;

public class RoleDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    // Atributos

    private Long id;
    private String authority;

    // Construtor sem argumentos

    public RoleDTO() {
    }

    // Construtor com argumentos

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    // Construtor com entidade

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.authority = role.getAuthority();
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

}
