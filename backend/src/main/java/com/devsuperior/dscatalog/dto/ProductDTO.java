package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    // Atributos

    private Long id;

    // minimo 5 caracteres e maximo 60
    @Size(min = 5, max = 60, message = "Mínimo 5 e máximo 60 caracteres")
    @NotBlank(message = "Campo obrigatório")
    private String name;

    @NotBlank(message = "Campo obrigatório")
    private String description;

    // O campo price só aceita valores positivos
    @Positive(message = "Deve ser um valor positivo")
    private Double price;

    private String imgUrl;

    @PastOrPresent(message = "A data não pode ser futura")
    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    // Construtor vazio

    public ProductDTO() {
    }

    // Construtor com argumentos

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
        // nao vamos colocar o categories no contrutor pois nao colocamos coleção em contrutores, nao trocamos coleção
    }

    // Contrutor que recebe uma entidade Product

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.date = entity.getDate();
    }

    // Construtor que recebe a entidade Product e um Set de categorias

    public ProductDTO(Product entity, Set<Category> categories) {

        // Pra nao repetir por ex. this.id = entity.getId();pra todos
        // vamos chamar o this(entity); que irá executar o construtor acima
        this(entity);

        // Acessamos o parametro categories que é uma Set, agora temos
        // que percorre-la e pra cada elemento dela instanciar um
        // CategoryDTO e inserir na lista private List<CategoryDTO> categories = new ArrayList<>();
        // pra isso vamos usar uma função lambda
        // forEach = pra cada cat desse Set que recebemos por parametro,
        // nós vamos lá na lista private List<CategoryDTO> categories = new ArrayList<>(); acessando assim this.categories
        // e adicionamos ele como uma instancia CategoryDTO assim add(new CategoryDTO(cat)));
        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));

    }

    // Getter e Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}
