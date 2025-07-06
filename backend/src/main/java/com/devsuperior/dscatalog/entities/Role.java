package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.util.Objects;

public class Role implements Serializable {

    private static final Long serialVersionUID = 1L;

    // Atributos

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
