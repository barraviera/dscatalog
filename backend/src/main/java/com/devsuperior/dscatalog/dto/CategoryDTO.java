package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Category;

import java.io.Serializable;

public class CategoryDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    // Atributos

    private Long id;
    private String name;

    // Construtor vazio

    public CategoryDTO() {

    }

    // Construtor com atributos

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Construtor que recebe a entidade

    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
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

}
