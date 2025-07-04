package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private Long dependentId;

    // Vamos usar pra converter um ProductDTO para um json pra passar na requisição put do
    // teste updateShouldReturnProductDTOWhenIdExist
    @Autowired
    private ObjectMapper objectMapper;

    // SIMULADOR ---

    // Simuladores, que executa antes de cada metodo de teste
    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

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

        // Simular update do ProductService com um id existente
        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        // Simular o update com um id inexistente
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        // Lembre-se: veja o metodo delete do ProductService e veja que seu retorno é void
        // entao 1º falamos a consequencia (doNothing) e depois o when
        // Entao fica: não faça nada quando chamar o delete com id existente
        doNothing().when(service).delete(existingId);
        // Simular delete quando o id é inexistente
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        // Lance a excessao DatabaseException quando chamar o metodo delete com um id que é dependente
        // ou seja, está em outra tabela e se apagado dará erro como falha de integridade
        doThrow(DatabaseException.class).when(service).delete(dependentId);
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

    // quando chamar o metodo update da classe ProductResource quando passar um id existente deveria retornar um ProductDTO
    @Test
    public void updateShouldReturnProductDTOWhenIdExist() throws Exception {

        // Converter um objeto ProductDTO em json
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        // o put é uma requisição que tem um corpo(body)
        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody) // corpo da requisição
                .contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
                .accept(MediaType.APPLICATION_JSON)); // tipo do retorno da resposta

        // Verificar se retornou um status Ok
        result.andExpect(status().isOk());
        // testando se o objeto que voltou na resposta tem id, name, description
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    // quando o id nao existe
    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        // Converter um objeto ProductDTO em json
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        // o put é uma requisição que tem um corpo(body)
        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody) // corpo da requisição
                .contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
                .accept(MediaType.APPLICATION_JSON)); // tipo do retorno da resposta

        // Verificar se retornou um status "não encontrado"
        result.andExpect(status().isNotFound());
    }

    @Test
    // Deverá retornar no content quando chamar o delete com um id existente
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        mockMvc.perform(delete("/products/{id}", existingId))
                .andExpect(status().isNoContent());
    }

    @Test
    // quando chamar o delete do ProductResource passando um id inexistente deveria retornar um ResourceNotFoundException
    public void deleteShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {

        // chamar a requisição do tipo delete passando um id inexistente
        ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId));

        // Verificar se retornou um status "não encontrado 404" ResourceNotFoundException
        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        mockMvc.perform(delete("/products/{id}", dependentId))
                .andExpect(status().isBadRequest());
    }


}
