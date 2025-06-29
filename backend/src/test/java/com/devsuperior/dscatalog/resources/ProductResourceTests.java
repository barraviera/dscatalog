package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Testes na camada web, ou seja, controller, ou resources como podemos chamar tambem
@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    // ATRIBUTOS ---

    @Autowired
    private MockMvc mockMvc;
    // Usar o @MockBean ao inves do @Mock, quando a classe de teste carrega o contexto da aplicação
    // e precisa mockar bean do sistema. Ex. quando usar o @WebMvcTest, usaremos o @MockBean
    @MockBean
    private ProductService service;
    // Usar o PageImpl pra instanciar um objeto concreto
    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;
    private Long existingId;
    private Long nonExistingId;

    // SIMULADOR ---

    // Simuladores, que executa antes de cada metodo de teste
    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;

        // Usamos a classe Factory que criamos para nos auxiliar
        productDTO = Factory.createProductDTO();
        // Criamos uma page passando uma lista de productDTO
        page = new PageImpl<>(List.of(productDTO));

        // Quando chamarmos o metodo findAllPaged do ProductService passando qualquer argumento any()
        // ele terá que retornar um objeto page do tipo PageImpl<ProductDTO>
        when(service.findAllPaged(any())).thenReturn(page);

        // Simular o comportamento do findById do ProductResource
        // Quando chamarmos o findById do ProductService passando um id existente ele deve retornar um ProductDTO
        when(service.findById(existingId)).thenReturn(productDTO);
        // Quando chamarmos o findById do ProductService passando um id inexistente ele deve lançar a excessao ResourceNotFoundException
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
    }

    // METODOS/TESTES ---

    @Test
    // o findAllPaged do ProductResource deveria retornar um page
    public void findAllShouldReturnPage() throws Exception {

        // o perform faz uma requisição e usando o andExpect para esperar que o status da resposta seja um isOk (200)
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    // quando chamar o findById do ProductResource passando um id existente deveria retornar um Product
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {

        // Fazemos a chamar na url /products/{id} passando um id existente
        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        // Verificar se retornou um status ok
        result.andExpect(status().isOk());
        // Verificar no corpo da resposta json se existe os campos
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    // quando chamar o findById do ProductResource passando um id inexistente deveria retornar um NotFound
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Fazemos a chamar na url /products/{id} passando um id inexistente
        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        // Verificar se retornou um status NotFound
        result.andExpect(status().isNotFound());
    }

}
