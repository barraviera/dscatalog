package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Teste de integração desde a camada web. Neste tipo de teste nós chamamos as outras camadas
// e até mesmo o banco de dados, ao inves de mocker por exemplo o service agora vamos chama-lo realmente

// Carrega o contexto da aplicação (teste de integração e web)
@SpringBootTest
// Trata as requisições sem subir o servidor
@AutoConfigureMockMvc
// A cada teste que rodar será feito um rollback no banco voltando ao original
@Transactional
public class ProductResourceITTests {

    // ATRIBUTOS ---

    @Autowired
    private MockMvc mockMvc;

    // Vamos usar pra converter um ProductDTO para um json pra passar na requisição put do
    // teste updateShouldReturnProductDTOWhenIdExist
    @Autowired
    private ObjectMapper objectMapper;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    // METODOS ---

    // Metodo que é executando antes de cada teste
    @BeforeEach
    void setUp() throws Exception {

        // Vamos definir valores pras variaveis
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25;
    }

    @Test
    // Testar ao chamar o metodo findAll do ProductResource deveria
    // retornar uma pagina ordenada quando eu mandar ordenar por nome
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {

        // chamar a requisição do tipo delete passando um id inexistente
        ResultActions result = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        // Esperar um status ok 200
        result.andExpect(status().isOk());
        // com o jsonPath nós conseguimos acessar o objeto da resposta, entao vamos querer o totalElements do json
        // entao vamos verificar se o valor de totalElements é igual ao countTotalProducts
        result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        // vamos verificar se no json retornado tem o campo content que é a lista de objetos
        // obs. verifique esses campos com o postman
        result.andExpect(jsonPath("$.content").exists());
        // vamos verificar se o content na posição zero no campo name tem o texto Macbook Pro
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExist() throws Exception {

        // Vamos criar um produto usando a classe Factory que haviamos criado
        ProductDTO productDTO = Factory.createProductDTO();

        // Converter um objeto ProductDTO em json
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        // Nome esperado apos atualizar
        // antes de fazer a requisição, vamos salvar este nome e testar depois se este nome está
        // no retorno do json
        String expectdName = productDTO.getName();
        String expectdDescription = productDTO.getDescription();

        // o put é uma requisição que tem um corpo(body)
        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody) // corpo da requisição
                .contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
                .accept(MediaType.APPLICATION_JSON)); // tipo do retorno da resposta

        // Verificar se retornou um status Ok
        result.andExpect(status().isOk());
        // testando se o objeto que voltou na resposta tem id, name, description
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(expectdName));
        result.andExpect(jsonPath("$.description").value(expectdDescription));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        // Vamos criar um produto usando a classe Factory que haviamos criado
        ProductDTO productDTO = Factory.createProductDTO();

        // Converter um objeto ProductDTO em json
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        // o put é uma requisição que tem um corpo(body)
        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody) // corpo da requisição
                .contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
                .accept(MediaType.APPLICATION_JSON)); // tipo do retorno da resposta

        // Verificar se retornou um status inNotFound
        result.andExpect(status().isNotFound());
    }

}
